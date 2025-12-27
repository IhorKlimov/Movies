package com.example.movies.ui.screens.home

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.api.model.MoviesResponse
import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.db.model.Genre
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.data.db.model.MovieWithGenreRef
import com.example.movies.data.repository.GenresRepository
import com.example.movies.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import com.example.movies.data.api.model.Movie as APIMovie

private const val logTag = "HomeViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val database: MovieDatabase,
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var currentPage = 1
    var isFetchEnabled by mutableStateOf(true)
        private set


    init {
        fetchMovieGenres()
        viewModelScope.launch {
            query.debounceIfNotEmpty().collect {
                currentPage = 1
                isFetchEnabled = true
                movies.clear()
                fetchMovies()
            }
        }
    }

    fun fetchMovies() {
        if (isLoading || !isFetchEnabled) return
        isLoading = true

        viewModelScope.launch {
            try {
                val q = query.value
                val result = if (q.isNotEmpty()) {
                    moviesRepository.searchMovie(
                        query = q,
                        page = currentPage
                    )
                } else {
                    moviesRepository.discover(currentPage)
                }

                if (result.isSuccessful) {
                    handleMovieResponseSuccess(result)
                } else {
                    error = result.errorBody()?.string()
                }
            } catch (e: Exception) {
                Log.e(logTag, "fetchMovies: $e")
                if (isRefreshing) {
                    movies.clear()
                }
                movies.addAll(database.movieDao().getAllMoviesWithGenres())
                isFetchEnabled = false
            }

            isLoading = false
            isRefreshing = false
        }
    }

    private fun fetchMovieGenres() {
        viewModelScope.launch {
            val numOfGenres = database.genreDao().getAll().size
            if (numOfGenres == 0) {
                val response = genresRepository.getMovieGenres()
                try {
                    if (response.isSuccessful) {
                        response.body()
                            ?.genres
                            ?.filterNotNull()
                            ?.map { Genre(it.id ?: -1, it.name) }
                            ?.let { database.genreDao().insertAll(it) }
                    }
                } catch (e: Exception) {
                    Log.e(logTag, "fetchMovieGenres: ", e)
                }
            }
        }
    }

    private suspend fun handleMovieResponseSuccess(result: Response<MoviesResponse>) {
        if (isRefreshing) {
            movies.clear()
        }

        val newPage = result.body()?.results
            ?.filterNotNull()
            .orEmpty()
            .filter { m -> movies.firstOrNull { it.movie.movieId == m.id } == null }

        saveToDatabase(newPage)

        movies.addAll(
            database.movieDao()
                .selectMoviesWithGenresByIds(newPage.mapNotNull { it.id })
        )

        if (currentPage == result.body()?.totalPages) {
            isFetchEnabled = false
        } else {
            currentPage++
        }
    }

    fun refresh() {
        isFetchEnabled = true
        currentPage = 1
        isRefreshing = true

        fetchMovies()
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    private suspend fun saveToDatabase(newPage: List<APIMovie>) {
        database.movieDao().insertAll(newPage.map { Movie.fromAPIModel(it) })
        database.movieWithGenreDao().insertAll(
            newPage.flatMap { movie ->
                movie.genreIds?.map { genre ->
                    MovieWithGenreRef(
                        movie.id ?: -1,
                        genre
                    )
                } ?: listOf()
            }
        )

    }

    private fun Flow<String>.debounceIfNotEmpty(): Flow<String> {
        return debounce {
            if (it.isEmpty()) 0 else 1000
        }
    }
}