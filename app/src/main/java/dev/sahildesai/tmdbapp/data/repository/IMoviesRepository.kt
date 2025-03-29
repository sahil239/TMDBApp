package dev.sahildesai.tmdbapp.data.repository

import androidx.paging.PagingData
import dev.sahildesai.tmdbapp.data.api.GetMovieResponse
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository{
    fun getMovies():  Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Long): ApiResult<Movie>
    suspend fun getSimilarMovies(movieId: Long): ApiResult<GetMovieResponse>
}
