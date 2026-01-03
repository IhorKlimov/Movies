package com.example.movies.data.repository.movies

import com.example.movies.data.api.model.DiscoverSortBy
import com.example.movies.data.db.model.MoviesResponse
import com.example.movies.data.repository.Result
import retrofit2.Response
import javax.inject.Inject
import com.example.movies.data.api.model.MoviesResponse as APIMoviesResponse

class MoviesRepositoryImpl @Inject constructor(
    private val localSource: MoviesLocalSource,
    private val remoteSource: MoviesRemoteSource
) : MoviesRepository {

    override suspend fun discover(
        page: Int,
        sortBy: DiscoverSortBy
    ): Result<MoviesResponse, String> {
        try {
            val response = remoteSource.discover(page, sortBy.apiValue)
            return handleResponse(response, page, sortBy)
        } catch (e: Exception) {
            return if (page == 1) localSource.getAllMovies(sortBy)
            else Result(null, e.toString())
        }
    }

    override suspend fun searchMovie(
        query: String,
        includeAdult: Boolean?,
        language: String?,
        primaryReleaseLanguage: String?,
        page: Int,
        region: String?,
        year: String?,
        sortBy: DiscoverSortBy
    ): Result<MoviesResponse, String> {
        try {
            val response = remoteSource.searchMovie(
                query,
                includeAdult,
                language,
                primaryReleaseLanguage,
                page,
                region,
                year
            )
            return handleResponse(response, page, sortBy)
        } catch (e: Exception) {
            return if (page == 1) localSource.getAllMovies(sortBy)
            else Result(null, e.toString())
        }
    }

    private suspend fun handleResponse(
        response: Response<APIMoviesResponse>,
        page: Int,
        sortBy: DiscoverSortBy
    ): Result<MoviesResponse, String> {
        if (response.isSuccessful) {
            val newPage = response.body()?.results?.filterNotNull().orEmpty()

            if (newPage.isEmpty()) return Result(MoviesResponse(listOf(), false))

            val savingResult = localSource.saveToDatabase(newPage)
            if (!savingResult.isSuccess) return Result(null, savingResult.error)

            return localSource.getMoviesByIds(
                newPage.mapNotNull { it.id },
                page,
                response.body()?.totalPages ?: -1,
                sortBy.sqlValue
            )
        } else {
            return if (page == 1) localSource.getAllMovies(sortBy)
            else Result(null, response.errorBody()?.string().orEmpty())
        }
    }
}