package com.example.trainfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
    private var userData: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var image = view.findViewById<ImageButton>(R.id.imageButton)
        auth = Firebase.auth
        database = Firebase.database.reference

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userData = dataSnapshot.getValue<User>()
                val reviewAdapter = ReviewAdapter(userData!!.posts)

                val recyclerView: RecyclerView = view.findViewById(R.id.profileRecyclerView)
                recyclerView.adapter = reviewAdapter
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



        val dataset = arrayOf("January", "February", "March")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("firebasestupid", "${userData.toString()}")
    }
}