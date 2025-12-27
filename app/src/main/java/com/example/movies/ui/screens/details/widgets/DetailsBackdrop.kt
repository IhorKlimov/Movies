package com.example.movies.ui.screens.details.widgets

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.example.movies.data.db.model.Movie


@Composable
fun DetailsBackdrop(
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