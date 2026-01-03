package com.example.movies.data.repository.movies

import com.example.movies.data.api.model.DiscoverSortBy
import com.example.movies.data.db.model.MoviesResponse
import com.example.movies.data.repository.Result

interface MoviesRepository {
    suspend fun discover(
        page: Int,
        sortBy: DiscoverSortBy
    ): Result<MoviesResponse, String>

    suspend fun searchMovie(
        query: String,
        includeAdult: Boolean? = null,
        language: String? = null,
        primaryReleaseLanguage: String? = null,
        page: Int,
        region: String? = null,
        year: String? = null,
        sortBy: DiscoverSortBy
    ): Result<MoviesResponse, String>
}