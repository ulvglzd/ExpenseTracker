package com.glzd.demo.testcrudapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TestCrudAppApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(TestCrudAppApplication.class, args);
	}



}
