package com.example.trainfinal

import android.net.Uri

data class User (
    val username: String? = null,
    val email: String? = null,
    val profileImage: Uri? = null,
    val posts: MutableList<String>? = mutableListOf(),
    val followers: MutableList<String>? = mutableListOf()
) {
}