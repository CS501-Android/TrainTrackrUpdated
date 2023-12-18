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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID

class RoutesFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var stopsRecyclerView: RecyclerView
    private var stars: ArrayList<ImageView> = ArrayList()
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
        stopsRecyclerView = view.findViewById(R.id.routeStops)

        auth = Firebase.auth
        database = Firebase.database.reference
        // Could be implemented better
        stars = ArrayList()
        stars.add(view.findViewById(R.id.star_1))
        stars.add(view.findViewById(R.id.star_2))
        stars.add(view.findViewById(R.id.star_3))
        stars.add(view.findViewById(R.id.star_4))
        stars.add(view.findViewById(R.id.star_5))
        Log.i("firebasestupid", stars.toString())

        for (i in 0..< stars.size) {
            stars[i].setOnClickListener {
                rating = i + 1
                resetStar()
                setStars(i)
            }
        }

        stopBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RouteStop())
                .addToBackStack("RoutesFragment")
                .commit()
        }

        getUserInformation(auth!!.currentUser!!.uid, database)
        getRoutes(database)
        updateRecycler()

        submitBtn.setOnClickListener {
            if (rating == 0) return@setOnClickListener
            if (viewModel.getData() == null || viewModel.getData()?.size == 0) return@setOnClickListener

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
            viewModel.updateData(ArrayList())

            resetStar()
            resetRecycler()

            Log.e("firebasestupid", "${userData.toString()}")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getUserInformation(userId: String, database: DatabaseReference) {
        database.child("users").child(userId).get().addOnSuccessListener {
            userData = it.getValue(User::class.java)
            Log.i("firebasestupid", userData.toString())
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

    private fun resetRecycler() {
        stopsRecyclerView.adapter = RouteStopAdapter(ArrayList()) {
        }
    }

    private fun updateRecycler() {
        if (viewModel.getData() != null) {
            stopsRecyclerView.adapter = RouteStopAdapter(viewModel.getData()!!.toMutableList()) {
                val data = viewModel.getData()
                data?.remove(it)
                viewModel.updateData(data!!)
                updateRecycler()
            }
        }
    }

    private fun resetStar() {
        for (star in stars) {
            star.setImageResource(R.drawable.star_hollow)
        }
    }

    private fun setStars(count: Int) {
        for (i in 0..count) {
            stars[i].setImageResource(R.drawable.star_filled)
        }
    }

}