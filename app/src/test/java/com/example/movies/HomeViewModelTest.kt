package com.example.movies

import com.example.movies.data.api.model.DiscoverSortBy
import com.example.movies.data.db.model.MovieWithGenre
import com.example.movies.data.db.model.MoviesResponse
import com.example.movies.data.repository.Result
import com.example.movies.data.repository.genres.GenresRepository
import com.example.movies.data.repository.movies.MoviesRepository
import com.example.movies.ui.screens.home.HomeViewModel
import com.example.movies.ui.screens.home.settings.DiscoverSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private val dispatcher = StandardTestDispatcher()

    private val moviesRepository: MoviesRepository = mockk(relaxed = true)
    private val genresRepository: GenresRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = HomeViewModel(moviesRepository, genresRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_shouldBeCorrect() {
        assertEquals(1, viewModel.currentPage)
        assertEquals(true, viewModel.movies.isEmpty())
        assertEquals(false, viewModel.isLoading)
        assertEquals(false, viewModel.isRefreshing)
        assertEquals(null, viewModel.error)
        assertEquals(true, viewModel.isFetchEnabled)
        assertEquals("", viewModel.query.value)
        assertEquals(
            DiscoverSettings(DiscoverSortBy.POPULARITY_DESC),
            viewModel.searchSettings.value
        )
    }

    @Test
    fun init() = runTest {
        assertEquals(false, viewModel.isLoading)

        coEvery { moviesRepository.discover(1, DiscoverSortBy.POPULARITY_DESC) } returns Result(
            MoviesResponse(listOf(mockk<MovieWithGenre>(relaxed = true)), true)
        )
        advanceUntilIdle()

        coVerify(exactly = 1) { genresRepository.fetchMovieGenres() }
        coVerify(exactly = 1) { moviesRepository.discover(1, DiscoverSortBy.POPULARITY_DESC) }

        assertEquals(false, viewModel.isLoading)
        assertEquals(1, viewModel.movies.size)
        assertEquals(2, viewModel.currentPage)
        assertEquals(true, viewModel.isFetchEnabled)
    }
}