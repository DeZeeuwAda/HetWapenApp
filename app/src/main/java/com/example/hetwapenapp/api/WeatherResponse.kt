package com.example.hetwapenapp.api

data class WeatherResponse(
    val current: Current
)

data class Current(
    val temperature: Int,
    val weather_descriptions: List<String>
)
