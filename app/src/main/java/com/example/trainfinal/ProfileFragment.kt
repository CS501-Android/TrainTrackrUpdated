package com.example.trainfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var image = view.findViewById<ImageButton>(R.id.imageButton)
        image.setOnClickListener {
//            photoDialog().show(childFragmentManager, "Photo Dialog")
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, Camera()).commit()
        }

        val dataset = arrayOf("January", "February", "March")
        val reviewAdapter = ReviewAdapter(dataset)

        val recyclerView: RecyclerView = view.findViewById(R.id.profileRecyclerView)
        recyclerView.adapter = reviewAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}