package com.example.movies.navigation

import androidx.navigation3.runtime.NavKey
import com.example.movies.data.db.model.Movie
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class MovieDetails(val movie: Movie) : NavKey