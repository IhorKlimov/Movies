package com.example.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.db.model.Genre

@Dao
interface GenreDao {

    @Query("SELECT * FROM Genre")
    suspend fun getAll(): List<Genre>

    @Query("SELECT * FROM Genre LIMIT 1")
    suspend fun getFirstGenre(): Genre?

    @Query(
        "SELECT * FROM MovieWithGenreRef " +
                "INNER JOIN Genre on Genre.genreId = MovieWithGenreRef.genreId " +
                "WHERE MovieWithGenreRef.movieId = :movieId"
    )
    suspend fun getMovieGenres(movieId: Int): List<Genre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<Genre>)
}