package com.example.movies.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.movies.data.db.model.Movie
import com.example.movies.ui.screens.details.widgets.DetailsBackdrop
import com.example.movies.ui.screens.details.widgets.DetailsDescription
import com.example.movies.ui.screens.details.widgets.DetailsHeader
import com.example.movies.ui.screens.details.widgets.TransparentAppBar
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movie: Movie,
    onBackPressed: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.Factory>(
        key = "${movie.movieId}",
        creationCallback = {
            it.create(movie.movieId ?: -1)
        })
) {
    val density = LocalDensity.current
    val systemBarHeight = ceil((WindowInsets.statusBars.getTop(density) / density.density)).dp

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(systemBarHeight)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    )
            )
        }
    ) { padding ->
        val scroll = rememberScrollState()
        Box {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(scroll),
            ) {
                Box {
                    DetailsBackdrop(movie)
                    DetailsHeader(movie, viewModel.genres)
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HorizontalDivider()
                    DetailsDescription(movie)
                }
            }
            TransparentAppBar(onBackPressed, modifier = Modifier.padding(top = systemBarHeight))
        }
    }
}
