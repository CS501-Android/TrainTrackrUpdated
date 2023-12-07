package com.example.trainfinal

import java.util.UUID

data class Review (
    val id: UUID? = null,
    val title: String? = null,
    // Maybe change it
    val content: String? = null,
    val rating: Float? = 0.0f
) {
}