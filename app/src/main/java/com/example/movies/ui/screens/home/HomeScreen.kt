package com.example.movies.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies.data.db.model.Movie
import com.example.movies.ui.screens.home.settings.DiscoverSettings
import com.example.movies.ui.screens.home.widgets.ErrorState
import com.example.movies.ui.screens.home.widgets.HomeAppBar
import com.example.movies.ui.screens.home.widgets.InitialLoadingState
import com.example.movies.ui.screens.home.widgets.SuccessState

@Composable
fun HomeScreen(
    onMovieSelected: (Movie) -> Unit,
    onSearchSettingsClick: (DiscoverSettings) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            HomeAppBar(
                viewModel.query.collectAsState().value,
                viewModel::onQueryChange,
                { onSearchSettingsClick(viewModel.searchSettings.value) }
            )
        }
    ) { padding ->
        if (viewModel.isLoading && viewModel.movies.isEmpty()) {
            InitialLoadingState(modifier = Modifier.padding(padding))
        } else if (viewModel.error != null) {
            ErrorState(viewModel.error.orEmpty(), Modifier.padding(padding))
        } else if (viewModel.movies.isNotEmpty()) {
            SuccessState(
                viewModel.movies,
                viewModel.isLoading,
                viewModel.isRefreshing,
                viewModel::fetchMovies,
                viewModel::refresh,
                onMovieSelected,
                Modifier.padding(padding)
            )
        }
    }
}