package com.example.trainfinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class RouteStopAdapter(private val dataSet: MutableList<RouteStops>):
RecyclerView.Adapter<RouteStopAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleView: TextView
        val latView: TextView
        val longView: TextView

        init {
            titleView = view.findViewById(R.id.title)
            latView = view.findViewById(R.id.lat)
            longView = view.findViewById(R.id.lon)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.stop_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleView.text = dataSet[position]?.title
        viewHolder.latView.text = dataSet[position]?.lat.toString()
        viewHolder.longView.text = dataSet[position]?.long.toString()
    }

    override fun getItemCount() = dataSet.size

}