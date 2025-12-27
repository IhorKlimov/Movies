package com.example.movies.ui.screens.home

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.api.model.DiscoverResponse
import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.db.model.Genre
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.data.db.model.MovieWithGenreRef
import com.example.movies.data.repository.GenresRepository
import com.example.movies.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import com.example.movies.data.api.model.Movie as APIMovie

private const val logTag = "HomeViewModel"

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
            fetchMovieGenres()

            try {
                val result = moviesRepository.discover(currentPage)

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

    private suspend fun fetchMovieGenres() {
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

    private suspend fun handleMovieResponseSuccess(result: Response<DiscoverResponse>) {
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

}