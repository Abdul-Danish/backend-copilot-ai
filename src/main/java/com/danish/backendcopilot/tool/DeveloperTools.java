package com.danish.backendcopilot.tool;

import java.time.LocalDateTime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.danish.backendcopilot.service.WeatherProperties;
import com.danish.backendcopilot.service.WeatherClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeveloperTools {

	private final WeatherProperties weatherProps;
	
	public DeveloperTools(WeatherProperties weatherProps) {
		this.weatherProps = weatherProps;
	}
	
	@Tool(description = "get the current weather conditions for given city")
	public WeatherClient.Response getCurrentWeather(String city) {
		log.info("calling weather api tool: {}", city);
		WeatherClient weatherService = new WeatherClient(weatherProps);
		return weatherService.apply(new WeatherClient.Request(city));
	}
	
	
	@Tool(description = "get the current date and time")
	public String getCurrentTime() {
		log.info("calling time tool");
		return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
	}
}
