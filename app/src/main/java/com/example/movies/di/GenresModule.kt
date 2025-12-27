package com.example.movies.di

import com.example.movies.data.network.AuthenticationInterceptor
import com.example.movies.data.repository.GenresRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenresModule {

    @Provides
    @Singleton
    fun getGenresRepository(): GenresRepository {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=UTF8".toMediaType())
            )
            .build()
            .create(GenresRepository::class.java)
    }
}