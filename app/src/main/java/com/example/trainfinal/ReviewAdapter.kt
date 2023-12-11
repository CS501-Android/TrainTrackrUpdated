package com.example.trainfinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val dataSet: MutableList<Route?>,
                    private val isFavorite: Boolean?,
                    val clickListener: (Route?) -> Unit):
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ratingView: TextView
        val titleView: TextView
        val star: ImageView
        val containerView: ConstraintLayout

        init {
            ratingView = view.findViewById(R.id.rating)
            titleView = view.findViewById(R.id.title)
            containerView = view.findViewById(R.id.containerView)
            star = view.findViewById(R.id.star)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):
            ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.review_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ratingView.text = dataSet[position]?.rating.toString()
        viewHolder.titleView.text = dataSet[position]?.routeTitle
        viewHolder?.containerView?.setOnClickListener { clickListener(dataSet[position]) }
        if (isFavorite != null && isFavorite == true) {
            viewHolder.star.setImageResource(R.drawable.star_filled)
        }
        Log.i("firebasedataset", "${dataSet[position]}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
