package com.chanshiyu.yuko;

import com.chanshiyu.yuko.utils.SpringUtil;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.chanshiyu.yuko.mapper")
@EnableSwagger2Doc
public class YukoApplication {

    @Bean
    public SpringUtil getSpringUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(YukoApplication.class, args);
    }

}
