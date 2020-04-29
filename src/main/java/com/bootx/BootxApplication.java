package com.bootx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BootxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootxApplication.class, args);
	}

}
