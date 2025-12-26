package com.example.movies.data.repository

import com.example.movies.data.api.model.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresRepository {
    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("language") language: String = "en"): Response<GenreResponse>
}