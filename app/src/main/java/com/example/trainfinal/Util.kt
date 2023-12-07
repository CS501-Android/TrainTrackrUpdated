package com.example.trainfinal

import com.google.firebase.database.DatabaseReference

class Util {
    companion object {
        fun writeNewUser(userId: String, username: String?, email: String?, database: DatabaseReference) {
            val user = User(username,
                email,
                null,
                mutableListOf(),
                mutableListOf())
            database
                .child("users")
                .child(userId)
                .setValue(user)
        }

    }
}