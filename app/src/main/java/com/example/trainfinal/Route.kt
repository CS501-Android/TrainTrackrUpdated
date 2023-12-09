package com.example.trainfinal

import android.net.Uri
import java.util.UUID

data class Route(
    val routeId: String = UUID.randomUUID().toString(),
    val routeTitle: String = "TMP",
    val routeDescription: String = "",
    val scene: Uri? = null,
    val rating: Float? = 0.0f,
    val reviewList: MutableList<Review> = mutableListOf(),
    val stops: MutableList<RouteStops> = mutableListOf()
) {
}
