package com.example.movies.data.repository

import com.example.movies.data.api.model.DiscoverResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.themoviedb.org/3/discover/movie
// ?include_adult=false
// &include_video=false
// &language=en-US
// &page=1
// &sort_by=popularity.desc



interface MoviesRepository {
    @GET("discover/movie")
    suspend fun discover(@Query("page") page: Int): Response<DiscoverResponse>
}