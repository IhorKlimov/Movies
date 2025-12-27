package com.example.movies.ui.screens.home.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre

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