package com.telegram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class RomanianCitizenshipChecker {
	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(RomanianCitizenshipChecker.class, args);
	}
}
