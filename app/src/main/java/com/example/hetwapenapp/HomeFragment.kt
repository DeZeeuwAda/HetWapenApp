package com.example.hetwapenapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hetwapenapp.R
import com.example.hetwapenapp.api.WeatherRepository

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var temperatureTextView: TextView
    private lateinit var weatherDescriptionTextView: TextView
    private lateinit var weatherRepository: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherRepository = WeatherRepository("47de1a58d30f7417a6911e2fa1d32d09")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        weatherDescriptionTextView = view.findViewById(R.id.weatherDescriptionTextView)

        view.findViewById<Button>(R.id.button_events).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_eventsFragment)
        }
        view.findViewById<Button>(R.id.button_drinks).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_drinksFragment)
        }
        view.findViewById<Button>(R.id.button_food).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foodFragment)
        }
        view.findViewById<Button>(R.id.button_about).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
        }

        // Haal weergegevens op voor Roosendaal
        fetchWeatherForCity("Roosendaal")
    }

    private fun fetchWeatherForCity(city: String) {
        weatherRepository.getWeatherData(city) { weatherModel ->
            weatherModel?.let {
                temperatureTextView.text = "${it.temperature}°C"
                weatherDescriptionTextView.text = it.description
            } ?: run {
                temperatureTextView.text = "Error"
                weatherDescriptionTextView.text = "Could not fetch weather data"
            }
        }
    }
}
