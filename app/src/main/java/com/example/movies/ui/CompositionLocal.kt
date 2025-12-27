package com.example.movies.ui

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf
import java.lang.IllegalStateException

val LocalSharedElementScope = compositionLocalOf<SharedTransitionScope> {
    throw IllegalStateException(
        "Unexpected access to LocalSharedElementScope. You should only " +
                "access LocalSharedElementScope inside a NavEntry passed " +
                "to NavDisplay."
    )
}