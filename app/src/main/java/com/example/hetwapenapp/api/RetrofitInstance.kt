package com.example.hetwapenapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherstack.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}
