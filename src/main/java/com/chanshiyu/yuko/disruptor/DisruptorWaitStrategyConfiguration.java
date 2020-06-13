package com.chanshiyu.yuko.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SHIYU
 * @description Disruptor
 * @since 2020-06-13
 */
@Configuration
public class DisruptorWaitStrategyConfiguration {

    @Bean
    @ConditionalOnMissingBean(WaitStrategy.class)
    public WaitStrategy getWaitStrategy() {
        // 如果 CPU 比较叼的话，可以用 YieldingWaitStrategy
        return new BlockingWaitStrategy();
    }

}
