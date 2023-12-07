package com.example.trainfinal

import android.net.Uri

data class Route(
    val lat: Float? = 0.0f,
    val long: Float? = 0.0f,
    val scene: Uri? = null
) {
}