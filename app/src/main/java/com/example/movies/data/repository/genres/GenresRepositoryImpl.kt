package com.example.movies.data.repository.genres

import android.util.Log
import com.example.movies.data.repository.Result
import javax.inject.Inject

private const val logTag = "GenresRepositoryImpl"

class GenresRepositoryImpl @Inject constructor(
    private val remoteSource: GenresRemoteSource,
    private val localSource: GenresLocalSource
) : GenresRepository {

    override suspend fun fetchMovieGenres(language: String): Result<Boolean, String> {
        return try {
            if (localSource.hasGenres()) {
                return Result(true)
            }

            val genres = remoteSource.getMovieGenres(language)
            if (genres.isSuccessful) {
                localSource.saveMovieGenres(genres.body()?.genres)
                Result(true)
            } else {
                Result(false, genres.errorBody()?.string())
            }
        } catch (e: Exception) {
            Log.e(logTag, "fetchMovieGenres: ", e)
            Result(false, e.toString())
        }
    }
}