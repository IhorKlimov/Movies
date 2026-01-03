package com.example.movies.data.api.model

import android.content.Context
import com.example.movies.R

enum class DiscoverSortBy(val apiValue: String, val sqlValue: String? = null) {
    ORIGINAL_TITLE_ASC("original_title.asc", "originalTitle ASC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.original_title_asc)
        }
    },
    ORIGINAL_TITLE_DESC("original_title.desc", "originalTitle DESC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.original_title_desc)
        }
    },
    POPULARITY_ASC("popularity.asc", "popularity ASC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.popularity_asc)
        }
    },
    POPULARITY_DESC("popularity.desc", "popularity DESC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.popularity_desc)
        }
    },
    REVENUE_ASC("revenue.asc") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.revenue_asc)
        }
    },
    REVENUE_DESC("revenue.desc") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.revenue_desc)
        }
    },
    PRIMARY_RELEASE_DATE_ASC("primary_release_date.asc", "releaseDate ASC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.primary_release_date_asc)
        }
    },
    PRIMARY_RELEASE_DATE_DESC("primary_release_date.desc", "releaseDate DESC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.primary_release_date_desc)
        }
    },
    TITLE_ASC("title.asc", "title ASC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.title_asc)
        }
    },
    TITLE_DESC("title.desc", "title DESC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.title_desc)
        }
    },
    VOTE_AVERAGE_ASC("vote_average.asc", "voteAverage ASC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.vote_average_asc)
        }
    },
    VOTE_AVERAGE_DESC("vote_average.desc", "voteAverage DESC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.vote_average_desc)
        }
    },
    VOTE_COUNT_ASC("vote_count.asc", "voteCount ASC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.vote_count_asc)
        }
    },
    VOTE_COUNT_DESC("vote_count.desc", "voteCount DESC") {
        override fun getReadableName(context: Context): String {
            return context.getString(R.string.vote_count_desc)
        }
    };

    abstract fun getReadableName(context: Context): String
}