package com.example.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomRawQuery
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenre

@Dao
interface MovieDao {
    @RawQuery
    suspend fun selectMoviesRaw(rawQuery: RoomRawQuery): List<Movie>

    @RawQuery
    suspend fun selectMoviesWithGenresRaw(rawQuery: RoomRawQuery): List<MovieWithGenre>

    @Query("SELECT * FROM movie WHERE movieId = :id")
    suspend fun selectById(id: Int): Movie

    suspend fun selectAll(sortBy: String) = selectMoviesRaw(
        RoomRawQuery(
            sql = "SELECT * FROM movie ORDER BY $sortBy"
        )
    )

    suspend fun selectByIds(ids: List<Int>, sortBy: String) = selectMoviesRaw(
        RoomRawQuery(
            sql = "SELECT * FROM movie WHERE movieId IN (${ids.joinToString()}) ORDER BY $sortBy"
        )
    )

    @Query("SELECT * FROM movie WHERE movieId = :id")
    suspend fun selectMovieWithGenresById(id: Int): MovieWithGenre

    suspend fun selectAllMoviesWithGenres(sortBy: String) = selectMoviesWithGenresRaw(
        RoomRawQuery(
            sql = "SELECT * FROM movie ORDER BY $sortBy"
        )
    )

    suspend fun selectMoviesWithGenresByIds(ids: List<Int>, sortBy: String) =
        selectMoviesWithGenresRaw(
            RoomRawQuery(
                sql = "SELECT * FROM movie WHERE movieId IN (${ids.joinToString()}) ORDER BY $sortBy"
            )
        )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Delete
    suspend fun delete(movie: Movie)
}
