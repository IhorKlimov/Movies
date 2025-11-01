package com.example.movies.ui.screens.home

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.model.Movie
import com.example.movies.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val logTag = "HomeViewModel"

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val database: MovieDatabase,
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    open val movies = mutableStateListOf<Movie>()
    open var isLoading by mutableStateOf(false)
        private set
    open var error by mutableStateOf<String?>(null)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var currentPage = 1
    var isFetchEnabled by mutableStateOf(true)
        private set

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        if (isLoading || !isFetchEnabled) return
        isLoading = true

        viewModelScope.launch {
          try {
              val result = moviesRepository.discover(currentPage)
              isLoading = false

              if (result.isSuccessful) {
                  val newPage = result.body()?.results
                      ?.filterNotNull()
                      .orEmpty()
                      .filter { m -> movies.firstOrNull { it.id == m.id } == null }
                  database.movieDao().insertAll(newPage)
                  movies.addAll(newPage)
                  if (currentPage == result.body()?.totalPages) {
                      isFetchEnabled = false
                  } else {
                      currentPage++
                  }
              } else {
                  error = result.errorBody()?.string()
              }
          } catch (e: Exception) {
              Log.e(logTag, "fetchMovies: $e")
              isLoading = false
              movies.addAll(database.movieDao().getAll())
              isFetchEnabled = false
          }
        }
    }

}