package com.chanshiyu.yuko;

import com.chanshiyu.yuko.disruptor.IDisruptorCommand;
import com.chanshiyu.yuko.disruptor.RingBufferWorkerPoolFactory;
import com.chanshiyu.yuko.disruptor.consumer.MessageConsumer;
import com.chanshiyu.yuko.disruptor.producer.MessageProducer;
import com.chanshiyu.yuko.disruptor.wapper.TranslatorDataWrapper;
import com.chanshiyu.yuko.utils.SpringUtil;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.stream.IntStream;

@SpringBootApplication
@MapperScan("com.chanshiyu.yuko.mapper")
@EnableSwagger2Doc
@EnableScheduling
public class YukoApplication {

    @Bean
    public SpringUtil getSpringUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(YukoApplication.class, args);

        // 启动 disruptor
        MessageConsumer[] consumers = new MessageConsumer[8];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new MessageConsumer();
            consumers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory factory = SpringUtil.getBean(RingBufferWorkerPoolFactory.class);
        factory.initAndStart(consumers);
    }


    private RingBufferWorkerPoolFactory getWorkerPoolFactory() {
        return SpringUtil.getBean(RingBufferWorkerPoolFactory.class);
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 3000)
    private void msg() {
        IntStream.range(1, 9).forEach(i -> {
            int command = i % 2 == 0 ? IDisruptorCommand.CHECK_MSG_HELLO : IDisruptorCommand.CHECK_MSG_HI;
            TranslatorDataWrapper wrapper = new TranslatorDataWrapper(command, "WORLD");
            MessageProducer messageProducer = getWorkerPoolFactory().getMessageProducer(command);
            messageProducer.publish(command, wrapper);
        });
    }

}
