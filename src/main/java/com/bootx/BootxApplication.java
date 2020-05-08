package com.bootx;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@NacosPropertySource(dataId = "config", autoRefreshed = true)
public class BootxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootxApplication.class, args);
	}

}
