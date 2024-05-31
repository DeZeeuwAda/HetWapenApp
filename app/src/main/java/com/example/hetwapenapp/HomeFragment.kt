package com.example.hetwapenapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment(R.layout.fragment_home) {

    // Het instellen van het navigeren van de homefragment naar de nieuwe fragments toe
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }
}
