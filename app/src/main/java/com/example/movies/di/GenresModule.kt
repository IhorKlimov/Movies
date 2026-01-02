package com.example.movies.di

import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.network.AuthenticationInterceptor
import com.example.movies.data.repository.genres.GenresLocalSource
import com.example.movies.data.repository.genres.GenresRemoteSource
import com.example.movies.data.repository.genres.GenresRepository
import com.example.movies.data.repository.genres.GenresRepositoryImpl
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
    fun getGenresRemoteSource(): GenresRemoteSource {
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
            .create(GenresRemoteSource::class.java)
    }

    @Provides
    @Singleton
    fun getGenresLocalSource(database: MovieDatabase): GenresLocalSource {
        return GenresLocalSource(database)
    }

    @Provides
    @Singleton
    fun getGenresRepository(
        localSource: GenresLocalSource,
        remoteSource: GenresRemoteSource
    ): GenresRepository {
        return GenresRepositoryImpl(remoteSource, localSource)
    }
}