package com.example.trainfinal

import android.app.Service
import android.content.Context
import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap<String, Route?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val popUpAlert = view.findViewById<CardView>(R.id.pop_up_new_alert)
//        val newAlertButton = view.findViewById<Button>(R.id.new_alert_button)
//        val submitAlertButton = view.findViewById<Button>(R.id.arrow_alert)

        auth = Firebase.auth
        database = Firebase.database.reference

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userData = dataSnapshot.getValue<User>()
                getRoutes(userData, view, database)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebaseStupid", "loadPost:onCancelled", databaseError.toException())
            }
        }

        database.child("users")
            .child(auth!!.currentUser!!.uid)
            .addValueEventListener(postListener)

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

    private fun getRoutes(user: User?, view: View, database: DatabaseReference) {
        var routes: MutableList<Route?> = mutableListOf()
        database.child("routes").get().addOnSuccessListener { it ->
            val children = it!!.children
            children.forEach {
                routeData[it.key.toString()] = it.getValue(Route::class.java)
                routes.add(it.getValue(Route::class.java))
            }
//            val reviewAdapter = ReviewAdapter(routes)
//            recyclerView.adapter = reviewAdapter
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }
}