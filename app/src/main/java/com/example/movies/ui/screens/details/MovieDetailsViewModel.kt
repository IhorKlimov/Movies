package com.example.movies.ui.screens.details

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.db.model.Genre
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

private const val logTag = "MovieDetailsViewModel"

@HiltViewModel(assistedFactory = MovieDetailsViewModel.Factory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private var database: MovieDatabase
) : ViewModel() {
    val genres = mutableStateListOf<Genre>()

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            Log.d(logTag, "fetchGenres: $movieId")
            val result = database.genreDao().getMovieGenres(movieId)
            genres.addAll(result)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieDetailsViewModel
    }
}