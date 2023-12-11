package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.internal.wait
import java.util.UUID

class RoutesFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap<String, Route?>()
    private var rating = 0;
    private val viewModel: StopViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_route, container, false)
        val submitBtn = view.findViewById<Button>(R.id.submit_route_button)
        val topic = view.findViewById<EditText>(R.id.title_input_field_routes)
        val review = view.findViewById<EditText>(R.id.review_input_field_routes)
        val stopBtn = view.findViewById<Button>(R.id.add_stop_button)
        auth = Firebase.auth
        database = Firebase.database.reference
        // Could be implemented better
        view.findViewById<ImageView>(R.id.star_1).setOnClickListener {
            rating = 1
            /*
                Change the image depending on selected or not.
                You can change this into a for loop / Arraylist such that all
                stars before is a certain way
             */
        }
        view.findViewById<ImageView>(R.id.star_2).setOnClickListener {
            rating = 2
        }
        view.findViewById<ImageView>(R.id.star_3).setOnClickListener {
            rating = 3
        }
        view.findViewById<ImageView>(R.id.star_4).setOnClickListener {
            rating = 4
        }
        view.findViewById<ImageView>(R.id.star_5).setOnClickListener {
            rating = 5
        }

        stopBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RouteStop())
                .addToBackStack("RoutesFragment")
                .commit()
        }

        Log.e("firebasestupid", viewModel.getData().toString())

        getUserInformation(auth!!.currentUser!!.uid, database)
        getRoutes(database)

        val mapView = childFragmentManager.findFragmentById(R.id.mapFragmentRoutes) as SupportMapFragment
        mapView.getMapAsync(this)

        submitBtn.setOnClickListener {
            if (rating == 0) return@setOnClickListener

            val topicText = topic.text.toString()
            val reviewText = review.text.toString()
            val newRoute = Route(UUID.randomUUID().toString(),
                topicText, reviewText, null, rating,
                mutableListOf(),
                viewModel.getData()!!.toMutableList())
            rating = 0
            topic.setText("")
            review.setText("")
            userData?.posts?.add(newRoute.routeId)
            routeData[newRoute.routeId] = newRoute

            Util.updateUser(auth!!.currentUser!!.uid, userData, database)
            Util.updateRoute(routeData, database)
            Log.e("firebasestupid", "${userData.toString()}")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.4383, -71.1856), 15f))
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
            }
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }

}