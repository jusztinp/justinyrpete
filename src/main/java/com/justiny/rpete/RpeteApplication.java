package com.justiny.rpete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RpeteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpeteApplication.class, args);
	}

}
