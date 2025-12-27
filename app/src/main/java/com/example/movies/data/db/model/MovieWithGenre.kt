package com.example.movies.data.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieWithGenreRef(
    val movieId: Int,
    val genreId: Int
)

data class MovieWithGenre(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(MovieWithGenreRef::class)
    )
    val genres: List<Genre>
)