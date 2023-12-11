package com.example.trainfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels

class RouteStop : Fragment() {
    private lateinit var title: EditText
    private lateinit var lat: EditText
    private lateinit var long: EditText
    private lateinit var submitBtn: Button
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
        submitBtn = view.findViewById(R.id.submit_stop)

        submitBtn.setOnClickListener {
            var stopList: ArrayList<RouteStops>? = ArrayList<RouteStops>()
            if (viewModel.getData() != null) stopList = viewModel.getData()
            val newStop = RouteStops(
                title.text.toString(),
                lat.text.toString().toFloat(),
                long.text.toString().toFloat()
            )
            title.setText("")
            lat.setText("")
            long.setText("")
            stopList?.add(newStop)
            viewModel.updateData(stopList!!)
        }

        return view
    }
}