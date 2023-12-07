package com.example.trainfinal

import java.util.UUID

data class Review (
    val id: UUID? = null,
    val title: String? = null,
    val content: String? = null,
    val routes: MutableList<Route> = mutableListOf(),
) {
}