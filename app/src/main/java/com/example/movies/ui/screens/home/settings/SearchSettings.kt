package com.example.movies.ui.screens.home.settings

import com.example.movies.data.api.model.DiscoverSortBy
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverSettings(
    val sortBy: DiscoverSortBy
)