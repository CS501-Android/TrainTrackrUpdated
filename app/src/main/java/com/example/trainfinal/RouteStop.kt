package com.example.trainfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import java.io.IOException

@Suppress("DEPRECATION")
class RouteStop : Fragment() {
    private lateinit var title: EditText
    private lateinit var lat: EditText
    private lateinit var long: EditText
    private lateinit var search: EditText
    private lateinit var submitBtn: Button
    private lateinit var searchBtn: Button
    private val viewModel: StopViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_route_stop, container, false)
        title = view.findViewById(R.id.input_field_title)
        lat = view.findViewById(R.id.input_field_lat_routes)
        long = view.findViewById(R.id.input_field_long_routes)
        search = view.findViewById(R.id.search)
        submitBtn = view.findViewById(R.id.submit_stop)
        searchBtn = view.findViewById(R.id.searchLocation)

        submitBtn.setOnClickListener {
            // Empty Check
            if (TextUtils.isEmpty(title.text.toString())) return@setOnClickListener
            if (TextUtils.isEmpty(lat.text.toString())) return@setOnClickListener
            if (TextUtils.isEmpty(long.text.toString())) return@setOnClickListener

            var stopList: ArrayList<RouteStops>? = ArrayList()
            if (viewModel.getData() != null) stopList = viewModel.getData()
            val newStop = RouteStops(
                title.text.toString(),
                lat.text.toString().toFloat(),
                long.text.toString().toFloat()
            )
            title.setText("")
            lat.setText("")
            long.setText("")
            search.setText("")
            stopList?.add(newStop)
            viewModel.updateData(stopList!!)
        }

        searchBtn.setOnClickListener {
            if (TextUtils.isEmpty(search.text.toString())) return@setOnClickListener
            val geoCoder = Geocoder(requireActivity())

            var addressList: List<Address>? = null
            try {
                addressList = geoCoder.getFromLocationName(search.text.toString(), 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val address = addressList!![0]
            title.setText(search.text.toString())
            lat.setText(address.latitude.toString())
            long.setText(address.longitude.toString())

            Log.i("firebasestupid", address.toString())
        }

        return view
    }
}