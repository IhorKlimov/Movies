package com.example.movies.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int? = null,
    val name: String? = null
)