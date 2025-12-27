package com.example.movies.data.network

import com.example.movies.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
                .build()
        )
    }
}