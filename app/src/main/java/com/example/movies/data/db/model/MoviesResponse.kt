package com.example.movies.data.db.model

data class MoviesResponse(
    val results: List<MovieWithGenre>,
    val hasMore: Boolean,
)