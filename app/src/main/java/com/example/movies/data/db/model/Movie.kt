package com.example.movies.data.db.model

import androidx.core.util.toHalf
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import com.example.movies.data.api.model.Movie as ApiMovie


@Entity
@Serializable
data class Movie(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    @PrimaryKey
    val movieId: Int? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null
) {
    @Ignore
    val fullPosterPath = "https://image.tmdb.org/t/p/w500/$posterPath"

    @Ignore
    val fullBackdropPath = "https://image.tmdb.org/t/p/w780/$backdropPath"

    @Ignore
    val voteForDisplay: String? = voteAverage?.let {
        val v = (it * 10).roundToInt() / 10f
        "$v / 10"
    }

    companion object {
        fun fromAPIModel(movie: ApiMovie): Movie {
            return Movie(
                adult = movie.adult,
                backdropPath = movie.backdropPath,
                movieId = movie.id,
                originalLanguage = movie.originalLanguage,
                originalTitle = movie.originalTitle,
                overview = movie.overview,
                popularity = movie.popularity,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount,
            )
        }
    }
}