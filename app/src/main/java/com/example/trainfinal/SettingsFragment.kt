package com.example.trainfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private lateinit var notificationsSwitch: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsSwitch = view.findViewById(R.id.notifications_switch)

        notificationsSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            // Handle the change in notification preference
            if (isChecked) {
                // Enable notifications
            } else {
                // Disable notifications
            }
        }
    }
}
