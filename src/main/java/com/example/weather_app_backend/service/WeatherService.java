package com.example.weather_app_backend.service;

import com.example.weather_app_backend.DTOs.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Map<String, String>> getWeather(String location) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=metric&appid=" + apiKey;

        Map<String, String> customResponse = new HashMap<>();
        try {
            WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

            if (weatherResponse != null && weatherResponse.getMain() != null && !weatherResponse.getWeather().isEmpty()) {
                customResponse.put("temperature", weatherResponse.getMain().getTemp() + " °C");
                customResponse.put("description", weatherResponse.getWeather().get(0).getDescription());
                return ResponseEntity.ok(customResponse);
            } else {
                customResponse.put("error", "Could not fetch weather data");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customResponse);
            }
        } catch (RestClientException e) {
            // Handle errors from the REST call
            String errorMessage = e.getMessage();
            if (errorMessage.contains("404")) {
                customResponse.put("error", "City not found. Please check the location.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customResponse);
            } else {
                customResponse.put("error", "An error occurred while fetching the weather data.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
            }
        }
    }
}
