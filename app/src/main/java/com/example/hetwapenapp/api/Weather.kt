package com.example.hetwapenapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Weather {
    @GET("current")
    fun getCurrentWeather(
        @Query("access_key") apiKey: String,
        @Query("query") city: String
    ): Call<WeatherResponse>
}