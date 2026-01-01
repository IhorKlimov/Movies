package com.example.movies.ui.screens.home.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.movies.data.api.model.DiscoverSortBy

class SearchSettingsViewModel : ViewModel() {
    val sortOptions = DiscoverSortBy.entries
    var selectedSortByIndex by mutableStateOf(sortOptions.indexOf(DiscoverSortBy.POPULARITY_DESC))
        private set

    fun setSortBy(index: Int) {
        selectedSortByIndex = index
    }

    val searchSettingsResult: DiscoverSettings
        get() = DiscoverSettings(
            sortOptions[selectedSortByIndex]
        )
}