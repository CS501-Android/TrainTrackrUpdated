package com.example.trainfinal

import android.net.Uri

data class User (
    val username: String = "",
    val email: String = "",
    val profileImage: Uri? = null,
    val posts: MutableList<String> = mutableListOf(),
    val followers: MutableList<String> = mutableListOf(),
    val notification: MutableList<String> = mutableListOf()
) {
}