package com.example.movies.ui.screens.home

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

    private var currentPage = 1
    var isFetchEnabled by mutableStateOf(true)
        private set

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        if (isLoading || !isFetchEnabled) return
        isLoading = true

        viewModelScope.launch {
            val result = moviesRepository.discover(currentPage)
            isLoading = false

            if (result.isSuccessful) {
                val newPage = result.body()?.results
                    ?.filterNotNull()
                    .orEmpty()
                    .filter { m -> movies.firstOrNull { it.id == m.id } == null }
                movies.addAll(newPage)
                if (currentPage == result.body()?.totalPages) {
                    isFetchEnabled = false
                } else {
                    currentPage++
                }
            } else {
                error = result.errorBody()?.string()
            }
        }
    }

}