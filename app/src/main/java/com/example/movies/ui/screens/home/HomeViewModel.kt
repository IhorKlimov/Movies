package com.example.movies.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.model.Movie
import com.example.movies.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val logTag = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    val movies = mutableStateListOf<Movie>()
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            val result = moviesRepository.discover(1)
            isLoading = false

            if (result.isSuccessful) {
                movies.addAll(result.body()?.results?.filterNotNull().orEmpty())
            } else {
                error = result.message()
                val m = result.errorBody()?.string()
                Log.d(logTag, "fetchMovies: $m")
            }
        }
    }

}