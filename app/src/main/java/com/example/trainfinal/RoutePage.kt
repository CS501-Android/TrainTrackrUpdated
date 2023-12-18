package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoutePage : Fragment(), OnMapReadyCallback {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var routeTitle: TextView
    private lateinit var routeDescription: TextView
    private lateinit var routeRecyclerView: RecyclerView
    private lateinit var favoriteBtn: ImageView
    private lateinit var mMap: GoogleMap
    private var routeStops: MutableList<RouteStops>? = ArrayList()
    private var routeId: String? = null
    private var isFavorite: Boolean? = null
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap<String, Route?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routeId = it.getString(ARG_PARAM1)
            isFavorite = it.getBoolean(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_route_page, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference
        routeTitle = view.findViewById(R.id.route_title)
        routeDescription = view.findViewById(R.id.route_description)
        routeRecyclerView = view.findViewById(R.id.route_recycler)
        favoriteBtn = view.findViewById(R.id.star)

        val mapView = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapView.getMapAsync(this)

        if (isFavorite == true)
            favoriteBtn.setImageResource(R.drawable.star_filled)

        // Set favorite
        favoriteBtn.setOnClickListener {
            if (userData!!.notification.contains(routeId)) {
                userData!!.notification.remove(routeId)
                favoriteBtn.setImageResource(R.drawable.star_hollow)
            } else {
                userData!!.notification.add(routeId.toString())
                favoriteBtn.setImageResource(R.drawable.star_filled)
            }
            Util.updateUser(auth.currentUser!!.uid, userData, database)
        }

        return view
    }

    private fun getUserInformation(userId: String, database: DatabaseReference) {
        database.child("users").child(userId).get().addOnSuccessListener {
            userData = it.getValue(User::class.java)
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }

    private fun getRoutes(database: DatabaseReference) {
        database.child("routes").get().addOnSuccessListener { it ->
            val children = it!!.children
            children.forEach {
                routeData[it.key.toString()] = it.getValue(Route::class.java)
                // Populate Adapter | Route Stop
                if (routeId == it.key.toString()) {
                    val value = it.getValue(Route::class.java)
                    routeTitle.text = value?.routeTitle.toString()
                    routeDescription.text = value?.routeDescription
                    routeStops = value?.stops
                    routeRecyclerView.adapter = RouteStopAdapter(value!!.stops) {
                        mMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(
                                    LatLng(
                                        it.lat!!.toDouble(),
                                        it.long!!.toDouble()
                                    ), 15f
                                )
                        )
                    }
                }

                val currentData = routeData[routeId]

                val polylineOptions = PolylineOptions()
                if (currentData?.stops != null && currentData.stops.size != 0) {
                    for (stop in currentData.stops) {
                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(stop.lat!!.toDouble(), stop.long!!.toDouble()))
                                .title(stop.title)
                        )

                        polylineOptions.add(
                            LatLng(stop.lat.toDouble(), stop.long.toDouble())
                        )
                    }

                    // Line Outline
                    mMap.addPolyline(polylineOptions)

                    mMap.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(
                                LatLng(
                                    currentData.stops[0].lat!!.toDouble(),
                                    currentData.stops[0].long!!.toDouble()
                                ), 15f
                            )
                    )
                }
            }
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(routeId: String, isFavorite: Boolean) =
            RoutePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, routeId)
                    putBoolean(ARG_PARAM2, isFavorite)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Set the map instance
        mMap = googleMap

        // Move to Belmont, MA | Default if no stops were added.
        googleMap.moveCamera(CameraUpdateFactory
            .newLatLngZoom(LatLng(42.4383, -71.1856), 15f))

        getUserInformation(auth.currentUser!!.uid, database)
        getRoutes(database)
    }
}