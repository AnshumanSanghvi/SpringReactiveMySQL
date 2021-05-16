package com.anshuman.spring.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@EnableAsync
public class SpringReactiveMySqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveMySqlApplication.class, args);
    }

}
