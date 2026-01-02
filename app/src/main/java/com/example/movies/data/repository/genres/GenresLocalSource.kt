package com.example.movies.data.repository.genres

import com.example.movies.data.db.MovieDatabase
import com.example.movies.data.db.model.Genre
import javax.inject.Inject
import com.example.movies.data.api.model.Genre as APIGenre

class GenresLocalSource @Inject constructor(
    private val database: MovieDatabase
) {
    suspend fun saveMovieGenres(genres: List<APIGenre?>?) {
        genres
            ?.filterNotNull()
            ?.map { Genre(it.id ?: -1, it.name) }
            ?.let { database.genreDao().insertAll(it) }
    }

    suspend fun hasGenres(): Boolean {
        return database.genreDao().getFirstGenre() != null
    }
}