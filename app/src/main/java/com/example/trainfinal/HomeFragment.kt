package com.example.trainfinal

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var retrofit: Retrofit
    private lateinit var weatherService: WeatherService


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Obtain the SupportMapFragment
        val mapView = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapView.getMapAsync(this)

        doAsync{
            getWeatherData()
        }.execute()
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Set the map instance
        mMap = googleMap

        // Move to Belmont, MA
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.4383, -71.1856), 15f))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val goingToDropDown = view.findViewById<CardView>(R.id.additional_popup)
        val comingFromText = view.findViewById<EditText>(R.id.location_lookup_text_start)
        var collapsiblePopUp = view.findViewById<CardView>(R.id.popup_collapsible)
        val bpCollapse = view.findViewById<Button>(R.id.button_collapsible)
        val verticalPopupLayout = view.findViewById<LinearLayout>(R.id.outer_lookup_layout)
        val horizontalPartOfThing = view.findViewById<LinearLayout>(R.id.horizontal_bar_search_location)
        val layoutParamsHorizontal = horizontalPartOfThing.layoutParams

        layoutParamsHorizontal.height = 0
        horizontalPartOfThing.layoutParams = layoutParamsHorizontal

        bpCollapse.setOnClickListener {
            val layoutParams = collapsiblePopUp.layoutParams
            if (layoutParams.height != 100) layoutParams.height = 100 else layoutParams.height = 820
            if (view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility == View.INVISIBLE)
                view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility = View.VISIBLE
                else view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility = View.INVISIBLE

            collapsiblePopUp.layoutParams = layoutParams
        }

        comingFromText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // The keyboard is being shown
                goingToDropDown.visibility = View.VISIBLE
                layoutParamsHorizontal.height = 200
                horizontalPartOfThing.layoutParams = layoutParamsHorizontal
            } else {
                // The keyboard is being hidden
            }
        }
    }

    private fun getWeatherData() {
        retrofit = Retrofit
            .Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        weatherService = retrofit.create(WeatherService::class.java)

        val x: Call<WeatherResponse> = weatherService.reverse(51.5098, -0.1180)

        x.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.i("weatherapp", t.toString())
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.i("weatherapp", response.body().toString())
            }

        })
    }
    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}


