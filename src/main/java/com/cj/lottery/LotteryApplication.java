package com.cj.lottery;

import cn.felord.payment.autoconfigure.EnableMobilePay;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.baomidou.mybatisplus.samples.quickstart.mapper","com.cj.lottery.dao.**"})
@SpringBootApplication
@EnableTransactionManagement
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }

}
