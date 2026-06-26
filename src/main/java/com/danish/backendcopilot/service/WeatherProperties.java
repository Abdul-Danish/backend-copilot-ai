package com.danish.backendcopilot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "weather")
public record WeatherProperties(String apiKey, String apiBaseUrl, String apiPath) {

}
