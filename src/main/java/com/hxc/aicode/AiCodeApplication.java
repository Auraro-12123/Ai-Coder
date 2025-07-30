package com.hxc.aicode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hxc.aicode.mapper")
public class AiCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeApplication.class, args);
    }

}
