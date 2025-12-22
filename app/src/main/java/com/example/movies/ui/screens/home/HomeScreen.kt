package com.example.movies.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.ui.LocalSharedElementScope


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieSelected: (Movie) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    with(LocalSharedElementScope.current) {
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
                    modifier = Modifier.renderInSharedTransitionScopeOverlay(1f)
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

@Composable
fun InitialLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorState(
    error: String,
    modifier: Modifier = Modifier
) {
    Text(error, modifier = modifier.padding(16.dp), color = MaterialTheme.colorScheme.error)
}

@Composable
fun SuccessState(
    movies: List<MovieWithGenre>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onLastItemVisible: () -> Unit,
    onRefresh: () -> Unit,
    onMovieSelected: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyGridState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        LazyVerticalGrid(
            state = state,
            modifier = Modifier.padding(horizontal = 4.dp),
            columns = GridCells.Adaptive(180.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = movies.size,
                key = { movies[it].movie.movieId ?: -1 }
            ) {
                val movie = movies[it]
                MovieOverview(movie, {
                    onMovieSelected(movie.movie)
                })
            }
            if (isLoading) {
                listProgressIndicator()
            }
        }
    }

    val isLastItemVisible by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                false
            } else {
                visibleItemsInfo.last().index == movies.size - 1
            }
        }
    }

    if (isLastItemVisible) {
        onLastItemVisible()
    }
}

private fun LazyGridScope.listProgressIndicator() {
    item(
        span = {
            GridItemSpan(maxLineSpan)
        }
    ) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun MovieOverview(
    movie: MovieWithGenre,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sharedElementScope = LocalSharedElementScope.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    with(sharedElementScope) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.movie.fullPosterPath)
                .crossfade(true)
                .placeholderMemoryCacheKey("movieImage${movie.movie.movieId ?: 0}")
                .memoryCacheKey("movieImage${movie.movie.movieId ?: 0}")
                .build(),
            modifier = modifier
                .sharedElement(
                    rememberSharedContentState("movieImage${movie.movie.movieId ?: 0}"),
                    animatedVisibilityScope
                )
                .aspectRatio(0.665f)
                .clickable(onClick = onClicked),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}