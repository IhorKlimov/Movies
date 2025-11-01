package com.example.movies

import com.example.movies.data.model.DiscoverResponse
import com.example.movies.data.model.Movie
import com.example.movies.data.repository.MoviesRepository
import com.example.movies.ui.screens.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private val dispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository.stub {
            onBlocking {
                discover(1)
            }.doReturn(
                Response.success(
                    DiscoverResponse(
                        1,
                        listOf(
                            Movie(
                                id = 1,
                                title = "Star Wars",
                                overview = "Demo overview"
                            )
                        ),
                        10,
                        100
                    )
                )
            )
        }

        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState() {
        assertEquals(1, viewModel.currentPage)
        assertEquals(true, viewModel.movies.isEmpty())
        assertEquals(true, viewModel.isLoading)
        assertEquals(null, viewModel.error)
        assertEquals(true, viewModel.isFetchEnabled)
    }

    @Test
    fun init() = runTest {
        assertEquals(false, viewModel.isLoading)

        advanceUntilIdle()

        assertEquals(false, viewModel.isLoading)
        assertEquals(1, viewModel.movies.size)
        assertEquals(2, viewModel.currentPage)
    }
}