package com.example.movies.ui.screens.details.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.movies.data.db.model.Genre
import com.example.movies.data.db.model.Movie
import com.example.movies.ui.LocalSharedElementScope
import com.example.movies.util.toFormattedDate


@Composable
fun DetailsHeader(movie: Movie, genres: List<Genre>, modifier: Modifier = Modifier) {
    val current = LocalWindowInfo.current

    val sharedElementScope = LocalSharedElementScope.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current
    val backdropHeight = current.containerDpSize.width / 1.77f

    Row(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        with(sharedElementScope) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.fullPosterPath)
                    .crossfade(true)
                    .placeholderMemoryCacheKey("movieImage${movie.movieId ?: 0}")
                    .memoryCacheKey("movieImage${movie.movieId ?: 0}")
                    .build(),
                modifier = Modifier
                    .padding(top = (backdropHeight - 48.dp))
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = backdropHeight)
            ) {
                Text(movie.title.orEmpty(), style = MaterialTheme.typography.titleLarge)
                Text(movie.releaseDate?.toFormattedDate().orEmpty())
                movie.voteForDisplay?.let {
                    Text(it)
                }
                if (genres.isNotEmpty()) {
                    Text(
                        genres.mapNotNull { it.name }.joinToString(transform = { it })
                    )
                }
            }
        }
    }
}