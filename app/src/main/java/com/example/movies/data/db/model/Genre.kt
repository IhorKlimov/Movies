package com.example.movies.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
    @PrimaryKey
    val genreId: Int,
    val name: String? = null
)
