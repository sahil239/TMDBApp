package dev.sahildesai.tmdbapp.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.sahildesai.tmdbapp.data.MoviesPagingSource
import dev.sahildesai.tmdbapp.data.api.APIService
import dev.sahildesai.tmdbapp.data.api.GetMovieResponse
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.data.repository.IMoviesRepository
import dev.sahildesai.tmdbapp.data.util.ApiResult
import dev.sahildesai.tmdbapp.data.util.parseAPICall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val apiService: APIService
): IMoviesRepository {
    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = true),
            pagingSourceFactory = { MoviesPagingSource(apiService, QUERY) }
        ).flow
    }

    override suspend fun getMovieDetails(movieId: Long): ApiResult<Movie> {
        return parseAPICall { apiService.getMovieDetails(movieId) }
    }

    override suspend fun getSimilarMovies(movieId: Long): ApiResult<GetMovieResponse> {
        delay(5000L)
        return parseAPICall { apiService.getSimilarMovies(movieId) }
    }

    private companion object{
        const val TAG = "MoviesRepository"
        const val QUERY = "71580"
    }
}