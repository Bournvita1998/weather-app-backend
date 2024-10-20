package com.example.weather_app_backend.DTOs;

import lombok.Data;

@Data
public class Weather {
    private String description;

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

