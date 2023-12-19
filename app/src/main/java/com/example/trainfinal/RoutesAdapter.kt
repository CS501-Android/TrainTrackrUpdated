package com.example.trainfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RoutesAdapter(private val routes: List<GoogleMapsRoute>,
                    private val confirmListener: (GoogleMapsRoute) -> Unit):
    RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val startAddressView: TextView = view.findViewById(R.id.startAddress)
        val endAddressView: TextView = view.findViewById(R.id.endAddress)
        val mapImageView: ImageView = view.findViewById(R.id.mapImageView)
        val confirmButton: TextView = view.findViewById(R.id.confirmButton)
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

            val staticMapUrl = getStaticMapUrl(firstLeg.start_location, firstLeg.end_location)
            Glide.with(viewHolder.itemView.context).load(staticMapUrl).into(viewHolder.mapImageView)
        }

        viewHolder.confirmButton.setOnClickListener { confirmListener(route) }
    }

    override fun getItemCount() = routes.size

    private fun getStaticMapUrl(startLocation: Location, endLocation: Location): String {
        val apiKey = "AIzaSyCq5ec_u8sOYk_llY8fVW2LBNvmYe_LGsU"
        val center = "${startLocation.lat},${startLocation.lng}"
        val zoomLevel = 14
        val size = "600x300"
        val markers = "color:red|label:S|${startLocation.lat},${startLocation.lng}|color:green|label:E|${endLocation.lat},${endLocation.lng}"

        return "https://maps.googleapis.com/maps/api/staticmap?center=$center&zoom=$zoomLevel&size=$size&markers=$markers&key=$apiKey"
    }

}

