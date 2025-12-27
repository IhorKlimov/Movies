package com.example.movies.ui.screens.home.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.ui.LocalSharedElementScope

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