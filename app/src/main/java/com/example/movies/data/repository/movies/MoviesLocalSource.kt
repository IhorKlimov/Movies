package com.example.movies.data.repository.movies

import android.util.Log
import com.example.movies.data.api.model.DiscoverSortBy
import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.data.db.model.MovieWithGenreRef
import com.example.movies.data.db.model.MoviesResponse
import com.example.movies.data.repository.Result
import javax.inject.Inject
import com.example.movies.data.api.model.Movie as APIMovie

private const val logTag = "MoviesLocalSource"

class MoviesLocalSource @Inject constructor(
    private val database: MovieDatabase
) {
    suspend fun saveToDatabase(newPage: List<APIMovie>): Result<Boolean, String> {
        return try {
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
            Result(true)
        } catch (e: Exception) {
            Result(false, e.toString())
        }
    }

    suspend fun getAllMovies(sortBy: DiscoverSortBy): Result<MoviesResponse, String> {
        return try {
            val dao = database.movieDao()
            val movies = dao.selectAllMoviesWithGenres(
                sortBy.sqlValue ?: DiscoverSortBy.POPULARITY_DESC.sqlValue.orEmpty()
            )
            Result(MoviesResponse(movies, false))
        } catch (e: Exception) {
            Log.e(logTag, "getAllMovies: ", e)
            Result(null, e.toString())
        }
    }

    suspend fun getMoviesByIds(
        ids: List<Int>,
        page: Int,
        totalPages: Int,
        sortBy: String?
    ): Result<MoviesResponse, String> {
        return try {
            val dao = database.movieDao()

            sortBy?.let { sortBy ->
                val movies = dao.selectMoviesWithGenresByIds(
                    ids,
                    sortBy
                )
                Result(MoviesResponse(movies, page < totalPages))
            } ?: run {
                // SQL value is null, meaning unsupported. Select using one by one with the API
                // order.
                val movies = mutableListOf<MovieWithGenre>()
                ids.forEach {
                    movies.add(dao.selectMovieWithGenresById(it))
                }
                Result(MoviesResponse(movies, page < totalPages))
            }
        } catch (e: Exception) {
            Log.e(logTag, "getMoviesByIds: ", e)
            Result(null, e.toString())
        }
    }

}