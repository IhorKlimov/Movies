package com.example.movies.di

import kotlinx.serialization.json.Json

internal val json = Json { ignoreUnknownKeys = true }
internal const val baseUrl = "https://api.themoviedb.org/3/"
