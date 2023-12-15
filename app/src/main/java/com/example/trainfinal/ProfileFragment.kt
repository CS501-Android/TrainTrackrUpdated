package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
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


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var username: TextView
    private lateinit var follower: TextView
    private lateinit var posts: TextView
    private var userData: User? = null
    private var routeData: HashMap<String, Route?> = HashMap<String, Route?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var image = view.findViewById<ImageButton>(R.id.imageButton)
        auth = Firebase.auth
        database = Firebase.database.reference
        recyclerView = view.findViewById(R.id.profileRecyclerView)
        posts = view.findViewById(R.id.posts)
        username = view.findViewById(R.id.username)
        follower = view.findViewById(R.id.follows)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userData = dataSnapshot.getValue<User>()
                posts.text = "${userData?.posts?.count()}"
                follower.text = "${userData?.followers?.count()}"
                username.text = "${userData?.email}"

                getRoutes(userData, database)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebaseStupid", "loadPost:onCancelled", databaseError.toException())
            }
        }

        database.child("users")
            .child(auth!!.currentUser!!.uid)
            .addValueEventListener(postListener)

//        image.setOnClickListener {
//            photoDialog().show(childFragmentManager, "Photo Dialog")
//            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, Camera()).commit()
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getRoutes(user: User?, database: DatabaseReference) {
        var routes: MutableList<Route?> = mutableListOf()
        database.child("routes").get().addOnSuccessListener { it ->
            val children = it!!.children
            children.forEach {
                routeData[it.key.toString()] = it.getValue(Route::class.java)
                if (user!!.posts.contains(it.key.toString())) {
                    routes.add(it.getValue(Route::class.java))
                }
            }
            recyclerView.adapter = ReviewAdapter(routes, null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RoutePage.newInstance(it?.routeId.toString(), false))
                    .addToBackStack(null)
                    .commit()
            }
        }.addOnFailureListener{
            Log.e("firebasestupid", "Error getting data", it)
        }
    }
}