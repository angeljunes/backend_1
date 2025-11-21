package com.rcas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RcasBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RcasBackendApplication.class, args);
    }
}
