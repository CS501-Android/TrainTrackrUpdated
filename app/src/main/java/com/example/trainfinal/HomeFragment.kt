package com.example.trainfinal

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var retrofit: Retrofit
    private lateinit var weatherService: WeatherService
    private lateinit var weatherText: TextView
    private val viewModel: TrainRouteViewModel by activityViewModels()


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
            getWeatherData(42.4383, -71.1856)
            viewModel.fetchTrainRoutes("US", "CAD", "00:00:00","2021-01-01")
            Log.i("weatherapp", "Error: ${viewModel.getError().toString()}")
            Log.i("weatherapp", "Routes: ${viewModel.getRoutes().toString()}")
        }.execute()
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Set the map instance
        mMap = googleMap

        Log.e("googlemapstupid", "hello")

        // Move to Belmont, MA
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.4383, -71.1856), 15f))

        googleMap.setOnCameraIdleListener {
            getWeatherData(mMap.cameraPosition.target.latitude, mMap.cameraPosition.target.longitude)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var collapsiblePopUp = view.findViewById<CardView>(R.id.popup_collapsible)
        val bpCollapse = view.findViewById<Button>(R.id.button_collapsible)
        weatherText = view.findViewById(R.id.weatherText)

        bpCollapse.setOnClickListener {
            val layoutParams = collapsiblePopUp.layoutParams
            if (layoutParams.height != 90) layoutParams.height = 90 else layoutParams.height = 820
            if (view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility == View.INVISIBLE)
                view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility = View.VISIBLE
                else view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility = View.INVISIBLE

            collapsiblePopUp.layoutParams = layoutParams
        }
    }

    private fun getWeatherData(lat: Double, lon: Double) {
        retrofit = Retrofit
            .Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        weatherService = retrofit.create(WeatherService::class.java)

        val x: Call<WeatherResponse> = weatherService.reverse(lon, lat)

        x.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.i("weatherapp", t.toString())
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val weatherDescription = response.body()?.weather?.get(0)?.description
                val kTemp = response.body()?.main?.temp
                val fTemp = ((kTemp!! - 273.15)  * 9 / 5 + 32)

                when (weatherDescription?.get(weatherDescription.length - 1)?.toChar()) {
                    'y' -> weatherText.text = "${round(fTemp)}°F\t | \tIt's a ${weatherDescription} one today!"
                    else -> weatherText.text = "${round(fTemp)}°F\t | \tIt's a ${weatherDescription}y one today!"
                }

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


