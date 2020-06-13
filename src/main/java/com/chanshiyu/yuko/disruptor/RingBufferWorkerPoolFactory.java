package com.chanshiyu.yuko.disruptor;

import com.chanshiyu.yuko.disruptor.consumer.MessageConsumer;
import com.chanshiyu.yuko.disruptor.producer.MessageProducer;
import com.chanshiyu.yuko.disruptor.wapper.TranslatorDataWrapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * @author SHIYU
 * @description 环型无锁队列
 * @since 2020-06-13
 */
@Slf4j
@Component
public class RingBufferWorkerPoolFactory {

    @Value("${disruptor.buffer.size}")
    private int mBufferSize;

    @Autowired
    private WaitStrategy mWaitStrategy;

    private Map<Integer, MessageProducer> producers = new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    public void initAndStart(MessageConsumer[] messageConsumers) {
        // 1.构建 ringBuffer 对象
        this.ringBuffer = RingBuffer.create(ProducerType.MULTI,
                TranslatorDataWrapper::new,
                mBufferSize,
                mWaitStrategy);
        // 2.通过 ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = this.ringBuffer.newBarrier();
        // 3.创建多个消费者数组
        WorkerPool<TranslatorDataWrapper> workerPool = new WorkerPool<>(
                this.ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                messageConsumers);
        // 4.设置多个消费者的 sequence 序号 用于单独统计消费进度, 并且设置到 ringBuffer 中
        this.ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        // 5.启动工作池
        int processorsCount = Runtime.getRuntime().availableProcessors();
        log.info("进程数 -> {}", processorsCount);
        workerPool.start(Executors.newFixedThreadPool(processorsCount));
    }

    public MessageProducer getMessageProducer(int command) {
        MessageProducer messageProducer = producers.get(command);
        if (messageProducer == null) {
            messageProducer = new MessageProducer(this.ringBuffer);
            producers.put(command, messageProducer);
        }
        return messageProducer;
    }

    /**
     * 异常静态类
     */
    @Slf4j
    static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWrapper> {

        @Override
        public void handleEventException(Throwable ex, long sequence, TranslatorDataWrapper event) {
            log.error("handleEventException -> ex:{}  sequence:{} event:{}", ex.getMessage(), sequence, event.getClass().toString());
            ex.printStackTrace();
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("handleOnStartException -> ex:{}", ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("handleOnShutdownException -> ex:{} ", ex.getMessage());
            ex.printStackTrace();
        }
    }

}
