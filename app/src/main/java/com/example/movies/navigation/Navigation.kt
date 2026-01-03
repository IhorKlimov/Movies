package com.example.movies.navigation

import androidx.navigation3.runtime.NavKey
import com.example.movies.data.db.model.Movie
import com.example.movies.ui.screens.home.settings.DiscoverSettings
import kotlinx.serialization.Serializable

@Serializable
data object HomeKey : NavKey

@Serializable
data class MovieDetails(val movie: Movie) : NavKey

@Serializable
data class SearchSettings(val settings: DiscoverSettings): NavKey