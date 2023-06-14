package com.project.homeFinder;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
@EnableAutoConfiguration
public class HomeFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeFinderApplication.class, args);
	}

}
