package com.example.trainfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment


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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}