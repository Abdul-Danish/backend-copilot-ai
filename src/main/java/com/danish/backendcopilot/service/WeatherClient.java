package com.danish.backendcopilot.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WeatherClient {

	private final WeatherProperties weatherProps;
	private final RestClient restClient;
	
	public WeatherClient(WeatherProperties weatherProps) {
		this.weatherProps = weatherProps;
		this.restClient = RestClient.builder().baseUrl(weatherProps.apiBaseUrl()).build();
	}
	
	public Response apply(Request request) {
		log.info("Fetching weather data for city: {}", request.city());
		Response response = restClient.get()
				.uri(UriBuilder -> UriBuilder
						.path(weatherProps.apiPath())
						.queryParam("q", request.city())
						.queryParam("key", weatherProps.apiKey())
						.build())
				.retrieve()
				.body(Response.class);
		
		log.info("weather api response: {}", response);
		return response;
	}
	
	public record Request (String city) {}
	public record Response (Location location, Current current) {}
	public record Location (String name) {}
	public record Current (float temp_c, float temp_f, Condition condition) {}
	public record Condition (String text) {}
}
