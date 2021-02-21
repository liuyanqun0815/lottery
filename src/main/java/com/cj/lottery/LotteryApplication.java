package com.cj.lottery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.baomidou.mybatisplus.samples.quickstart.mapper","com.cj.lottery.dao.**"})
@SpringBootApplication
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }

}
