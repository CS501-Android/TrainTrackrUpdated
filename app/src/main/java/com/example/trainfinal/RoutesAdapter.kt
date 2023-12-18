package com.example.trainfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoutesAdapter(private val routes: List<Route>,
                    val clickListener: (GoogleMapsRoute) -> Unit):
    RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val startAddressView: TextView = view.findViewById(R.id.startAddress)
        val endAddressView: TextView = view.findViewById(R.id.endAddress)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.route_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val route = routes[position]
        if (route.legs.isNotEmpty()) {
            val firstLeg = route.legs.first()
            viewHolder.startAddressView.text = firstLeg.start_address
            viewHolder.endAddressView.text = firstLeg.end_address
        }
        viewHolder.itemView.setOnClickListener { clickListener(route) }
    }

    override fun getItemCount() = routes.size
}
