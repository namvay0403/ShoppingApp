package com.nam.ShoppingApp;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ShoppingAppApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext appContext;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String[] beans = appContext.getBeanDefinitionNames();
		Arrays.sort(beans);
		for (String bean : beans) {
			System.out.println(bean);
		}

	}

}
