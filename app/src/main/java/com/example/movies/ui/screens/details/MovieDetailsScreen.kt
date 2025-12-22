package com.example.movies.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movies.R
import com.example.movies.data.db.model.Movie
import com.example.movies.ui.LocalSharedElementScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(movie: Movie, onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(movie.title.orEmpty())
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            painterResource(R.drawable.outline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        val scroll = rememberScrollState()
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scroll),
        ) {
            Header(movie)
            HorizontalDivider()
            Description(movie)
        }
    }
}

@Composable
private fun Header(movie: Movie, modifier: Modifier = Modifier) {
    val sharedElementScope = LocalSharedElementScope.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    with(sharedElementScope) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.fullPosterPath)
                    .crossfade(true)
                    .placeholderMemoryCacheKey("movieImage${movie.movieId ?: 0}")
                    .memoryCacheKey("movieImage${movie.movieId ?: 0}")
                    .build(),
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState("movieImage${movie.movieId ?: 0}"),
                        animatedVisibilityScope
                    )
                    .aspectRatio(0.665f)
                    .weight(1f),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(movie.title.orEmpty(), style = MaterialTheme.typography.titleLarge)
                Text(movie.releaseDate.orEmpty())
                movie.voteAverage?.let {
                    Text("$it/10")
                }
            }
        }
    }
}

@Composable
private fun Description(movie: Movie, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        movie.overview?.let { Text(it) }
    }
}