package com.example.movies.di

import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.network.AuthenticationInterceptor
import com.example.movies.data.repository.movies.MoviesLocalSource
import com.example.movies.data.repository.movies.MoviesRemoteSource
import com.example.movies.data.repository.movies.MoviesRepository
import com.example.movies.data.repository.movies.MoviesRepositoryImpl
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
object MoviesModule {
    @Singleton
    @Provides
    fun getMoviesRemoteSource(): MoviesRemoteSource {
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
            .create(MoviesRemoteSource::class.java)
    }

    @Singleton
    @Provides
    fun getMoviesLocalSource(database: MovieDatabase): MoviesLocalSource {
        return MoviesLocalSource(database)
    }

    @Singleton
    @Provides
    fun getMoviesRepository(
        localSource: MoviesLocalSource,
        remoteSource: MoviesRemoteSource
    ): MoviesRepository {
        return MoviesRepositoryImpl(localSource, remoteSource)
    }
}