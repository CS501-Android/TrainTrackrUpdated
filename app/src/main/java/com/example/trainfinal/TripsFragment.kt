package com.example.trainfinal

import android.app.Service
import android.content.Context
import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.fragment.app.Fragment

class TripsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popUpAlert = view.findViewById<CardView>(R.id.pop_up_new_alert)
        val newAlertButton = view.findViewById<Button>(R.id.new_alert_button)
        val submitAlertButton = view.findViewById<Button>(R.id.arrow_alert)

        popUpAlert.visibility = View.INVISIBLE

        newAlertButton.setOnClickListener {
            if (popUpAlert.visibility != View.VISIBLE) {
                popUpAlert.visibility = View.VISIBLE
            }
        }

        submitAlertButton.setOnClickListener {
            // Submit the alert: Create an Alert object with the destinations
            // and add to a RecyclerView
            popUpAlert.visibility = View.INVISIBLE
        }
    }
}