package com.chanshiyu.yuko.disruptor.producer;

import com.chanshiyu.yuko.disruptor.wapper.TranslatorDataWrapper;
import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SHIYU
 * @description 消息生产者
 * @since 2020-06-13
 */
@Data
@Slf4j
@AllArgsConstructor
public class MessageProducer {

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    /**
     * 发布事件
     *
     * @param command 命令字
     * @param object 数据
     */
    public void publish(int command, Object object) {
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setCommand(command);
            wrapper.setTarget(object);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
