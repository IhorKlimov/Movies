package com.example.movies.data.db

import androidx.room.AutoMigration
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.data.model.Movie
import java.lang.ProcessBuilder.Redirect.to


@Dao
interface MovieDao {
    @Query("SELECT * from movie")
    suspend fun getAll(): List<Movie>

    @Query("SELECT * FROM movie where id in (:ids)")
    suspend fun selectByIds(ids: IntArray): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Delete
    suspend fun delete(movie: Movie)
}

@Database(
    entities = [Movie::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

