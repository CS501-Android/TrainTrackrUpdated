package com.example.trainfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.w3c.dom.Text

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

        bpCollapse.setOnClickListener {
//            collapsiblePopUp.layoutParams.width = 50
            Toast.makeText(context, "Hmm...", Toast.LENGTH_SHORT).show()
            collapsiblePopUp.layoutParams.height = 50
            val cardViewWidth = resources.getDimension(R.dimen.card_view_width)

            cardView.layoutParams.width = cardViewWidth.toInt()

        }

        goingToDropDown.visibility = View.INVISIBLE


        comingFromText.setOnClickListener {
           goingToDropDown.visibility = View.VISIBLE
//            val myView = view.findViewById<EditText>(R.id.outer_lookup_layout)
//            val layoutParams = myView.layoutParams
//            layoutParams.width = 80
//            myView.layoutParams = layoutParams
        }
    }

}
