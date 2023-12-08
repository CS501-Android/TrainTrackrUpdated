package com.example.trainfinal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private lateinit var moonIcon: ImageView
    private lateinit var sunIcon: ImageView
    private lateinit var accountNav: Button
    private lateinit var reviewNav: Button
    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moonIcon = view.findViewById(R.id.moon_icon)
        sunIcon = view.findViewById(R.id.sun_icon)
        logoutBtn = view.findViewById(R.id.sign_out)
        var sunOn = true

        accountNav =  view.findViewById(R.id.nav_account_settings)
        reviewNav = view.findViewById(R.id.nav_review_settings)

        moonIcon.setOnClickListener {
            if (sunOn) {
                sunOn = false // Replace with database call in the future
                moonIcon.setImageResource(R.drawable.moon_filled)
                sunIcon.setImageResource(R.drawable.sunny_hollow)
            }
        }

        sunIcon.setOnClickListener {
            if (!sunOn) {
                sunOn = true // Replace with database call in the future
                moonIcon.setImageResource(R.drawable.moon_hollow)
                sunIcon.setImageResource(R.drawable.sunny_filled)
            }
        }

        accountNav.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            // Create an instance of the target fragment
            val targetFragment = AccountDetailsFragment()

            // Replace the current fragment with the target fragment
            transaction.replace(R.id.fragment_container, targetFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        reviewNav.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            // Create an instance of the target fragment
            val targetFragment = ReviewFragment()

            // Replace the current fragment with the target fragment
            transaction.replace(R.id.fragment_container, targetFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(activity, Login::class.java))
        }
    }
}
