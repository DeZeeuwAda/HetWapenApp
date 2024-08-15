package com.example.hetwapenapp.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(private val apiKey: String) {

    private val weatherApi = RetrofitInstance.api

    fun getWeatherData(city: String, callback: (WeatherModel?) -> Unit) {
        weatherApi.getCurrentWeather(apiKey, city).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val weatherModel = WeatherModel(
                            temperature = it.current.temperature,
                            description = it.current.weather_descriptions.firstOrNull() ?: "No description"
                        )
                        callback(weatherModel)
                    } ?: run {
                        Log.e("WeatherRepository", "Empty response body")
                        callback(null)
                    }
                } else {
                    Log.e("WeatherRepository", "API call failed with response code: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("WeatherRepository", "API call failed: ${t.message}")
                callback(null)
            }
        })
    }
}
