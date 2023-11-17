package com.shangan.tradelightningdeal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.shangan"})
@MapperScan({"com.shangan.tradelightningdeal.db.mappers"})
@SpringBootApplication
public class TradeLightningDealApplication {

    public static void main(String[] args) {

        SpringApplication.run(TradeLightningDealApplication.class, args);
    }

}
