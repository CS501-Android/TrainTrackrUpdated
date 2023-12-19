package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RoutesFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var stopsRecyclerView: RecyclerView
    private var stars: ArrayList<ImageView> = ArrayList()
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap()
    private var rating = 0;
    private val viewModel: StopViewModel by activityViewModels()
    private lateinit var originEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var searchRouteButton: Button
    private lateinit var routesRecyclerView: RecyclerView
    private val client = OkHttpClient()
    private lateinit var routesAdapter: RoutesAdapter
    private var routesList: MutableList<GoogleMapsRoute> = mutableListOf()

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

        originEditText = view.findViewById(R.id.origin_input_field)
        destinationEditText = view.findViewById(R.id.destination_input_field)
        searchRouteButton = view.findViewById(R.id.search_route_button)
        routesRecyclerView = view.findViewById(R.id.routes_recycler_view)

        searchRouteButton.setOnClickListener {
            val origin = originEditText.text.toString()
            val destination = destinationEditText.text.toString()
            if (origin.isNotBlank() && destination.isNotBlank()) {
                searchRoute(origin, destination)
            } else {
                Toast.makeText(context, "Please enter both origin and destination", Toast.LENGTH_SHORT).show()
            }
        }

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

        setupRecyclerView(view)

        return view
    }

    private fun searchRoute(origin: String, destination: String) {
        val apiKey = "AIzaSyCq5ec_u8sOYk_llY8fVW2LBNvmYe_LGsU"
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&mode=transit&transit_mode=train&key=$apiKey"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(activity, "Failed to fetch routes: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val responseData = response.body?.string()
                response.body?.close()
                val gson = Gson()
                val directionsResult = gson.fromJson(responseData, DirectionsApiResponse::class.java)

                activity?.runOnUiThread {
                    routesList.clear()
                    routesList.addAll(directionsResult.routes)
                    routesAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupRecyclerView(view: View) {
        routesRecyclerView = view.findViewById(R.id.routes_recycler_view)
        routesAdapter = RoutesAdapter(routesList) { selectedRoute ->
            confirmRoute(selectedRoute)
        }
        routesRecyclerView.adapter = routesAdapter
    }

    private fun confirmRoute(route: GoogleMapsRoute) {
        val appRoute = convertGoogleMapsRouteToAppRoute(route)
        storeRouteInFirebase(appRoute)
        Toast.makeText(context, "Route confirmed and added", Toast.LENGTH_SHORT).show()

        val index = routesList.indexOf(route)
        if (index != -1) {
            routesList.removeAt(index)
            routesAdapter.notifyItemRemoved(index)
        }
    }

    private fun convertGoogleMapsRouteToAppRoute(googleMapsRoute: GoogleMapsRoute): Route {
        val firstLeg = googleMapsRoute.legs.firstOrNull()
        val lastLeg = googleMapsRoute.legs.lastOrNull()

        val routeTitle = "Route from ${firstLeg?.start_address} to ${lastLeg?.end_address}"
        val routeDescription = "This route includes ${googleMapsRoute.legs.size} legs."

        val stops = googleMapsRoute.legs.flatMap { leg ->
            leg.steps.map { step ->
                RouteStops(
                    title = step.html_instructions,
                    lat = step.start_location.lat.toFloat(),
                    long = step.start_location.lng.toFloat()
                )
            }
        }

        return Route(
            routeTitle = routeTitle,
            routeDescription = routeDescription,
            stops = stops.toMutableList()
        )
    }

    private fun storeRouteInFirebase(route: Route) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val database = FirebaseDatabase.getInstance().reference
            Util.updateRoute(mapOf(route.routeId to route), database)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchRouteButton.setOnClickListener {
            val origin = originEditText.text.toString()
            val destination = destinationEditText.text.toString()
            if (origin.isNotBlank() && destination.isNotBlank()) {
                searchRoute(origin, destination)
            } else {
                Toast.makeText(context, "Please enter both origin and destination", Toast.LENGTH_SHORT).show()
            }
        }
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