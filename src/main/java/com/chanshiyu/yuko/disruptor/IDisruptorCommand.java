package com.chanshiyu.yuko.disruptor;

/**
 * @author SHIYU
 * @description 无锁队列命令字
 * @since 2020-06-13
 */
public interface IDisruptorCommand {

    /**
     * 测试消息 hello
     */
    int CHECK_MSG_HELLO = 1;

    /**
     * 测试消息 hi
     */
    int CHECK_MSG_HI = 2;

}
