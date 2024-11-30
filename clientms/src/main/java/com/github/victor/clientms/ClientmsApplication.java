package com.github.victor.clientms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClientmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientmsApplication.class, args);
	}

}
