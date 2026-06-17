package com.example.waiterapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WaiterAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaiterAppApplication.class, args);
    }
}
