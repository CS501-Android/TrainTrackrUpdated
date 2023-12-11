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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [RoutePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoutePage : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var routeTitle: TextView
    private lateinit var routeDescription: TextView
    private lateinit var routeRecyclerView: RecyclerView
    private lateinit var favoriteBtn: ImageView
    private var routeStops: MutableList<RouteStops>? = ArrayList()
    private var routeId: String? = null
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap<String, Route?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routeId = it.getString(ARG_PARAM1)
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
        getUserInformation(auth.currentUser!!.uid, database)
        getRoutes(database)

        // Set favorite
        favoriteBtn.setOnClickListener {
            if (userData!!.notification.contains(routeId)) {
                userData!!.notification.remove(routeId)
            } else {
                userData!!.notification.add(routeId.toString())
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
                }
            }
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment routePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(routeId: String) =
            RoutePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, routeId)
                }
            }
    }
}