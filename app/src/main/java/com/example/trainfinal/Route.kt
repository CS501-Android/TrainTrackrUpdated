package com.example.trainfinal

import android.net.Uri
import java.util.UUID

data class Route(
    val routeId: UUID? = null,
    val routeTitle: String? = null,
    val routeDescription: String? = null,
    val scene: Uri? = null,
    val rating: Float? = 0.0f,
    val reviewList: MutableList<Review> = mutableListOf(),
    val stops: MutableList<RouteStops> = mutableListOf()
) {
}