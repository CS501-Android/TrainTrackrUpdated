package com.example.trainfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

// MainActivity
class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private val tripsFragment = TripsFragment()
    private val settingsFragment = SettingsFragment()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragment_container, homeFragment)
            .commit()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_trips -> {
                    navigateToTrips()
                    true
                }

                R.id.nav_map -> {
                    navigateToHome()
                    true
                }

                R.id.nav_profile -> {
                    navigateToProfile()
                    true
                }

                else -> {
                    navigateToSettings()
                    true
                }
            }
        }
    }

    private fun navigateToHome() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToProfile() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, profileFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToTrips() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, tripsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToSettings() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, settingsFragment)
            .addToBackStack(null)
            .commit()
    }
}
