package com.example.trainfinal

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// MainActivity
class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    private val tripsFragment = TripsFragment()
    private val settingsFragment = SettingsFragment()
    private val routeFragment = RoutesFragment()
    private lateinit var bottomNavigationView: BottomNavigationView

    private val requiredPerms =
        mutableListOf(android.Manifest.permission.CAMERA,
//            android.Manifest.permission.READ_EXTERNAL_STORAGE,
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .toTypedArray()

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            var permissionGranted = true
            permissions.entries.forEach{
                if (it.key in requiredPerms && !it.value)
                    permissionGranted = false
            }

            if (!permissionGranted)
                Toast.makeText(this, "Permission request Denied", Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this, "Permission request Granted", Toast.LENGTH_LONG).show()
                Thread.sleep(500)
                finish()
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pages = arrayListOf("trips", "map", "profile", "route", "settings")

        if (!hasPerms(baseContext)) {
            activityResultLauncher.launch(requiredPerms)
        } else {
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

                    R.id.nav_route -> {
                        navigateToRoutes()
                        true
                    }

                    else -> {
                        navigateToSettings()
                        true
                    }
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

    private fun navigateToRoutes() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, routeFragment)
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

    private fun hasPerms(context: Context) = requiredPerms.all {
        ContextCompat
            .checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
