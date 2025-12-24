package com.example.movies.ui.screens.details

import android.view.WindowManager
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
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
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scroll),
        ) {
            Box {
                Backdrop(movie)
                Header(movie)
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HorizontalDivider()
                Description(movie)
            }
        }
    }
}

@Composable
private fun Backdrop(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = movie.fullBackdropPath,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.77f)
    )
}

@Composable
private fun Header(movie: Movie, modifier: Modifier = Modifier) {
    val current = LocalConfiguration.current

    val sharedElementScope = LocalSharedElementScope.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current
    val backdropHeight = current.screenWidthDp / 1.77f

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        with(sharedElementScope) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.fullPosterPath)
                    .crossfade(true)
                    .placeholderMemoryCacheKey("movieImage${movie.movieId ?: 0}")
                    .memoryCacheKey("movieImage${movie.movieId ?: 0}")
                    .build(),
                modifier = Modifier
                    .padding(start = 16.dp, top = (backdropHeight - 48).dp)
                    .weight(1f)
                    .dropShadow(RoundedCornerShape(4.dp), Shadow(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .aspectRatio(0.665f)
                    .sharedElement(
                        rememberSharedContentState("movieImage${movie.movieId ?: 0}"),
                        animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f).padding(top = (backdropHeight + 16).dp)) {
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