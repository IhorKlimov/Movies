package com.example.movies.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.data.db.dao.GenreDao
import com.example.movies.data.db.dao.MovieDao
import com.example.movies.data.db.dao.MovieWithGenreDao
import com.example.movies.data.db.model.Genre
import com.example.movies.data.db.model.Movie
import com.example.movies.data.db.model.MovieWithGenreRef


@Database(
    entities = [Movie::class, Genre::class, MovieWithGenreRef::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieWithGenreDao(): MovieWithGenreDao
}
