package com.example.movies.data.repository.genres

import com.example.movies.data.repository.Result

interface GenresRepository {
    suspend fun fetchMovieGenres(language: String = "en"): Result<Boolean, String>
}