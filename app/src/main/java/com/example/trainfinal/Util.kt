package com.example.trainfinal

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

class Util {
    companion object {
        fun writeNewUser(userId: String, username: String, email: String, database: DatabaseReference) {
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

        fun updateUser(userId: String, user: User?, database: DatabaseReference) {
            database
                .child("users")
                .child(userId)
                .setValue(user)
        }

    }
}