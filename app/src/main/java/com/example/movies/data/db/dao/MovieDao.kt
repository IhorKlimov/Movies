package com.example.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre

@Dao
interface MovieDao {
    @Query("SELECT * from movie")
    suspend fun getAll(): List<Movie>

    @Transaction
    @Query("SELECT * FROM movie")
    suspend fun getAllMoviesWithGenres(): List<MovieWithGenre>

    @Query("SELECT * FROM movie where movieId in (:ids)")
    suspend fun selectByIds(ids: IntArray): List<Movie>

    @Query("SELECT * FROM movie where movieId in (:ids)")
    suspend fun selectMoviesWithGenresByIds(ids: List<Int>): List<MovieWithGenre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Delete
    suspend fun delete(movie: Movie)
}
