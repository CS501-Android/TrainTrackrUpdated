package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TripsFragment : Fragment() {
    private lateinit var recommendationRecyclerView: RecyclerView
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap<String, Route?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trips, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference
        favoriteRecyclerView = view.findViewById(R.id.favorite_recycler)
        recommendationRecyclerView = view.findViewById(R.id.recommend_recycler)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userData = dataSnapshot.getValue<User>()
                getRoutes(userData, database)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebaseStupid", "loadPost:onCancelled", databaseError.toException())
            }
        }

        database.child("users")
            .child(auth!!.currentUser!!.uid)
            .addValueEventListener(postListener)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val popUpAlert = view.findViewById<CardView>(R.id.pop_up_new_alert)
//        val newAlertButton = view.findViewById<Button>(R.id.new_alert_button)
//        val submitAlertButton = view.findViewById<Button>(R.id.arrow_alert)

//        popUpAlert.visibility = View.INVISIBLE
//
//        newAlertButton.setOnClickListener {
//            if (popUpAlert.visibility != View.VISIBLE) {
//                popUpAlert.visibility = View.VISIBLE
//            }
//        }
//
//        submitAlertButton.setOnClickListener {
//            // Submit the alert: Create an Alert object with the destinations
//            // and add to a RecyclerView
//            popUpAlert.visibility = View.INVISIBLE
//        }
    }

    private fun getRoutes(user: User?, database: DatabaseReference) {
        var routes: MutableList<Route?> = mutableListOf()
        var favoriteRoute: MutableList<Route?> = mutableListOf()
        database.child("routes").get().addOnSuccessListener { it ->
            val children = it!!.children
            children.forEach {
                routeData[it.key.toString()] = it.getValue(Route::class.java)
                if (user!!.notification.contains(it.key.toString())) {
                    favoriteRoute.add(it.getValue(Route::class.java))
                } else {
                    routes.add(it.getValue(Route::class.java))
                }
                Log.i("firebasestupid2", routes.toString())
            }

            recommendationRecyclerView.adapter = ReviewAdapter(routes, false) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RoutePage.newInstance(it?.routeId.toString(), false))
                    .addToBackStack(null)
                    .commit()
            }

            favoriteRecyclerView.adapter = ReviewAdapter(favoriteRoute, true) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RoutePage.newInstance(it?.routeId.toString(), true))
                    .addToBackStack(null)
                    .commit()
            }
//            val reviewAdapter = ReviewAdapter(routes)
//            recyclerView.adapter = reviewAdapter
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }
}