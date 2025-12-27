package com.example.movies.ui.screens.home

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.example.movies.data.db.model.Movie
import com.example.movies.ui.LocalSharedElementScope
import com.example.movies.ui.screens.home.widgets.ErrorState
import com.example.movies.ui.screens.home.widgets.InitialLoadingState
import com.example.movies.ui.screens.home.widgets.SuccessState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieSelected: (Movie) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    with(LocalSharedElementScope.current) {
        with(LocalNavAnimatedContentScope.current) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Movies")
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(1f)
                            .animateEnterExit(
                                enter = fadeIn() + slideInVertically {
                                    it
                                },
                                exit = fadeOut() + slideOutVertically {
                                    it
                                }
                            )
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
    }
}