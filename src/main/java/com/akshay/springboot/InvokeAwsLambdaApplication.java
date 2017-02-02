package com.akshay.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.akshay.controller"})
public class InvokeAwsLambdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvokeAwsLambdaApplication.class, args);
	}
}
