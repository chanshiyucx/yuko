package com.chanshiyu.yuko.disruptor.consumer;


import com.chanshiyu.yuko.disruptor.IDisruptorCommand;
import com.chanshiyu.yuko.disruptor.wapper.TranslatorDataWrapper;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费者
 *
 * @author nk
 */
@Slf4j
public class MessageConsumer implements WorkHandler<TranslatorDataWrapper> {

    @Override
    public void onEvent(TranslatorDataWrapper wrapper) {
        int command = wrapper.getCommand();
        switch (command) {
            case IDisruptorCommand.CHECK_MSG_HELLO:
                log.info("消费消息 =============== hello");
                break;
            case IDisruptorCommand.CHECK_MSG_HI:
                log.info("消费消息 =============== hi");
                break;
            default:
                break;
        }
    }

}
