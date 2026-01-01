package com.example.movies.data.repository

import com.example.movies.data.api.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesRepository {
    @GET("discover/movie")
    suspend fun discover(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String
    ): Response<MoviesResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean? = null,
        @Query("language") language: String? = null,
        @Query("primary_release_language") primaryReleaseLanguage: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null,
        @Query("year") year: String? = null,
    ): Response<MoviesResponse>
}
