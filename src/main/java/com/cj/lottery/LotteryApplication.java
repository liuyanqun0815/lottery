package com.cj.lottery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.baomidou.mybatisplus.samples.quickstart.mapper","com.cj.lottery.dao.**"})
//@MapperScan({"com.cj.lottery.dao.**"})
//@ComponentScan(basePackages="com.cj.lottery.**")

@SpringBootApplication
//@EnableTransactionManagement
public class LotteryApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LotteryApplication.class);
    }


}
