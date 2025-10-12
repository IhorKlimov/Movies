package com.example.movies.ui.screens.home

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.movies.data.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Movies")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (viewModel.isLoading) {
            LoadingState(modifier = Modifier.padding(padding))
        } else if (viewModel.error != null) {
            ErrorState(viewModel.error.orEmpty(), Modifier.padding(padding))
        } else if (viewModel.movies.isNotEmpty()) {
            SuccessState(viewModel.movies, Modifier.padding(padding))
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
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
fun SuccessState(movies: List<Movie>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = 4.dp),
        columns = GridCells.Adaptive(180.dp),
        contentPadding = PaddingValues(vertical = 8.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = movies.size,
            key = { movies[it].id ?: -1 }
        ) {
            MovieOverview(movies[it])
        }
    }
}

@Composable
fun MovieOverview(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
        modifier = modifier,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}