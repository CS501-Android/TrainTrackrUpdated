package com.example.trainfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Obtain the SupportMapFragment
        val mapView = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Set the map instance
        mMap = googleMap

        // Move to Belmont, MA
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.4383, -71.1856), 15f))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val goingToDropDown = view.findViewById<CardView>(R.id.additional_popup)
        val comingFromText = view.findViewById<EditText>(R.id.location_lookup_text_start)
        var collapsiblePopUp = view.findViewById<CardView>(R.id.popup_collapsible)
        val bpCollapse = view.findViewById<Button>(R.id.button_collapsible)
        val verticalPopupLayout = view.findViewById<LinearLayout>(R.id.outer_lookup_layout)
        val horizontalPartOfThing = view.findViewById<LinearLayout>(R.id.horizontal_bar_search_location)
        val layoutParamsHorizontal = horizontalPartOfThing.layoutParams

        layoutParamsHorizontal.height = 0
        horizontalPartOfThing.layoutParams = layoutParamsHorizontal

        bpCollapse.setOnClickListener {
            val layoutParams = collapsiblePopUp.layoutParams
            if (layoutParams.height != 100) layoutParams.height = 100 else layoutParams.height = 820
            if (view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility == View.INVISIBLE)
                view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility = View.VISIBLE
                else view.findViewById<LinearLayout>(R.id.layout_bottom_home_details).visibility = View.INVISIBLE

            collapsiblePopUp.layoutParams = layoutParams
        }

        comingFromText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // The keyboard is being shown
                goingToDropDown.visibility = View.VISIBLE
                layoutParamsHorizontal.height = 200
                horizontalPartOfThing.layoutParams = layoutParamsHorizontal
            } else {
                // The keyboard is being hidden

            }
        })
    }
}
