package com.example.trainfinal

import android.net.Uri
import java.util.UUID

data class Route(
    val routeId: String = UUID.randomUUID().toString(),
    val routeTitle: String = "TMP",
    val routeDescription: String = "",
    val scene: Uri? = null,
    val rating: Int? = 0,
    val reviewList: MutableList<Review> = mutableListOf(),
    val stops: MutableList<RouteStops> = mutableListOf()
) {
}
