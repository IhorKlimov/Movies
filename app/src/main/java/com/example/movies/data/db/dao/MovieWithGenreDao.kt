package com.example.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.example.movies.data.db.model.MovieWithGenreRef

@Dao
interface MovieWithGenreDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(items: List<MovieWithGenreRef>)
}