package com.example.trainfinal

import android.net.Uri

data class User (
    val username: String = "",
    val email: String = "",
    val profileImage: Uri? = null,
    val posts: MutableList<Route> = mutableListOf(),
    val followers: MutableList<String> = mutableListOf()
) {
}