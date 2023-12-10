package com.example.trainfinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ReviewAdapter(private val dataSet: MutableList<Route?>, val clickListener: (Route?) -> Unit):
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ratingView: TextView
        val titleView: TextView
        val containerView: ConstraintLayout

        init {
            ratingView = view.findViewById(R.id.rating)
            titleView = view.findViewById(R.id.title)
            containerView = view.findViewById(R.id.containerView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):
            ReviewAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.review_item, viewGroup, false)

        return ReviewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ReviewAdapter.ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ratingView.text = dataSet[position]?.rating.toString()
        viewHolder.titleView.text = dataSet[position]?.routeTitle
        viewHolder?.containerView?.setOnClickListener { clickListener(dataSet[position]) }
        Log.i("firebasedataset", "${dataSet[position]}")
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
