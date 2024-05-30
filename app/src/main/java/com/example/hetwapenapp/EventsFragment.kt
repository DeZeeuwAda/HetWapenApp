package com.example.hetwapenapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class EventsFragment : Fragment(R.layout.fragment_events) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event1Title: TextView = view.findViewById(R.id.titel1)
        val event1Description: TextView = view.findViewById(R.id.beschrijving1)
        val event2Title: TextView = view.findViewById(R.id.titel2)
        val event2Description: TextView = view.findViewById(R.id.beschrijving2)

        event1Title.setOnClickListener {
            if (event1Description.visibility == View.GONE) {
                event1Description.visibility = View.VISIBLE
            } else {
                event1Description.visibility = View.GONE
            }
        }

        event2Title.setOnClickListener {
            if (event2Description.visibility == View.GONE) {
                event2Description.visibility = View.VISIBLE
            } else {
                event2Description.visibility = View.GONE
            }
        }
    }
}
