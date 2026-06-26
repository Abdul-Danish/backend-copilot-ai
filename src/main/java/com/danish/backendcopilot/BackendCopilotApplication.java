package com.danish.backendcopilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.danish.backendcopilot.service.WeatherProperties;

@EnableConfigurationProperties(value = {WeatherProperties.class})
@SpringBootApplication
public class BackendCopilotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendCopilotApplication.class, args);
	}

}
