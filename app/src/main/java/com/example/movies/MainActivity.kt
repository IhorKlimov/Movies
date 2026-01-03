package com.example.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.movies.navigation.HomeKey
import com.example.movies.navigation.MovieDetails
import com.example.movies.navigation.SearchSettings
import com.example.movies.navigation.rememberResultStore
import com.example.movies.ui.LocalSharedElementScope
import com.example.movies.ui.screens.details.MovieDetailsScreen
import com.example.movies.ui.screens.home.HomeScreen
import com.example.movies.ui.screens.home.HomeViewModel
import com.example.movies.ui.screens.home.settings.DiscoverSettings
import com.example.movies.ui.screens.home.settings.SearchSettingsScreen
import com.example.movies.ui.theme.MoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(HomeKey)
            val resultStore = rememberResultStore()

            MoviesTheme {
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSharedElementScope provides this@SharedTransitionLayout
                    ) {
                        NavDisplay(
                            backStack = backStack,
                            onBack = { backStack.removeLastOrNull() },
                            entryProvider = entryProvider {
                                entry<HomeKey> {
                                    val viewModel = viewModel<HomeViewModel>()
                                    resultStore.getResult<DiscoverSettings>()?.let {
                                        viewModel.onSearchSettingsChange(it)
                                    }

                                    HomeScreen(
                                        { backStack.add(MovieDetails(it)) },
                                        { backStack.add(SearchSettings(it)) }
                                    )
                                }
                                entry<MovieDetails> {
                                    MovieDetailsScreen(it.movie, {
                                        backStack.removeLastOrNull()
                                    })
                                }
                                entry<SearchSettings> {
                                    SearchSettingsScreen({
                                        resultStore.setResult(it)
                                        backStack.removeLastOrNull()
                                    })
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
