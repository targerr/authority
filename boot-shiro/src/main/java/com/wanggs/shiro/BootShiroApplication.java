package com.wanggs.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.wanggs.shiro.mappers"})
public class BootShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootShiroApplication.class, args);
    }

}
