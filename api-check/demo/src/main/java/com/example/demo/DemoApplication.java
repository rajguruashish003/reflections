package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.sql.DataSource;

@SpringBootApplication
@PropertySources({@PropertySource(value = "classpath:local.properties")})

public class DemoApplication {

	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
