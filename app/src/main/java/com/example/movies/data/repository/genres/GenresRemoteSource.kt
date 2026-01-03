package com.example.movies.data.repository.genres

import com.example.movies.data.api.model.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresRemoteSource {
    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("language") language: String): Response<GenreResponse>
}