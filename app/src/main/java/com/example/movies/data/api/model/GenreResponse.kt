package com.example.movies.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val genres: List<Genre?>? = null
)