package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.internal.wait
import java.util.UUID

class RoutesFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userData: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_route, container, false)
        val submitBtn = view.findViewById<Button>(R.id.submit_button_add_route)
        val topic = view.findViewById<EditText>(R.id.topic_edit_text)
        val review = view.findViewById<EditText>(R.id.review_edit_text)
        auth = Firebase.auth
        database = Firebase.database.reference

        getUserInformation(auth!!.currentUser!!.uid, database)

        val mapView = childFragmentManager.findFragmentById(R.id.mapFragmentRoutes) as SupportMapFragment
        mapView.getMapAsync(this)

        submitBtn.setOnClickListener {
            val topicText = topic.text.toString()
            val reviewText = review.text.toString()
            topic.setText("")
            review.setText("")
            val newRoute = Route(UUID.randomUUID().toString(), topicText, reviewText)
            userData?.posts?.add(newRoute)
            Util.updateUser(auth!!.currentUser!!.uid, userData, database)
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

}