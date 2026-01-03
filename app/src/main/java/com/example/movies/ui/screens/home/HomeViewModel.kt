package com.example.movies.ui.screens.home

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.api.model.DiscoverSortBy
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.data.db.model.MoviesResponse
import com.example.movies.data.repository.genres.GenresRepository
import com.example.movies.data.repository.movies.MoviesRepository
import com.example.movies.ui.screens.home.settings.DiscoverSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val logTag = "HomeViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val genresRepository: GenresRepository
) : ViewModel() {
    open val movies = mutableStateListOf<MovieWithGenre>()
    open var isLoading by mutableStateOf(false)
        private set
    var isRefreshing by mutableStateOf(false)
    open var error by mutableStateOf<String?>(null)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _searchSettings = MutableStateFlow(
        DiscoverSettings(
            DiscoverSortBy.POPULARITY_DESC
        )
    )
    val searchSettings: StateFlow<DiscoverSettings> = _searchSettings

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var currentPage = 1
    var isFetchEnabled by mutableStateOf(true)
        private set


    init {
        fetchMovieGenres()
        viewModelScope.launch {
            query.debounce {
                if (it.isEmpty()) 0 else 1000
            }.collect {
                fetchMoviesFromBeginning()
            }
        }
        viewModelScope.launch {
            searchSettings.drop(1).collect {
                fetchMoviesFromBeginning()
            }
        }
    }

    fun fetchMovies() {
        if (isLoading || !isFetchEnabled) return
        isLoading = true

        viewModelScope.launch {
            val q = query.value
            val result = if (q.isNotEmpty()) {
                moviesRepository.searchMovie(
                    query = q,
                    page = currentPage,
                    sortBy = searchSettings.value.sortBy
                )
            } else {
                moviesRepository.discover(currentPage, searchSettings.value.sortBy)
            }

            val body = result.body
            if (result.isSuccess && body != null) {
                handleMovieResponseSuccess(body)
            } else {
                error = result.error
            }

            isLoading = false
            isRefreshing = false
        }
    }

    fun fetchMoviesFromBeginning(isRefresh: Boolean = false) {
        isFetchEnabled = true
        currentPage = 1
        if (isRefresh) {
            isRefreshing = true
        } else {
            movies.clear()
        }
        fetchMovies()
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onSearchSettingsChange(searchSettings: DiscoverSettings) {
        _searchSettings.value = searchSettings
    }

    private fun fetchMovieGenres() {
        viewModelScope.launch {
            genresRepository.fetchMovieGenres()
        }
    }

    private fun handleMovieResponseSuccess(response: MoviesResponse) {
        if (isRefreshing) movies.clear()

        val newPage = response.results
            .filter { m -> movies.firstOrNull { it.movie.movieId == m.movie.movieId } == null }

        movies.addAll(newPage)

        if (response.hasMore) {
            currentPage++
        } else {
            isFetchEnabled = false
        }
    }
}