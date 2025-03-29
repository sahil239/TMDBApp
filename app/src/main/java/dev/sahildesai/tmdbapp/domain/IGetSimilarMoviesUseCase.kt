package dev.sahildesai.tmdbapp.domain

import dev.sahildesai.tmdbapp.data.api.GetMovieResponse
import dev.sahildesai.tmdbapp.data.util.ApiResult

interface IGetSimilarMoviesUseCase {
    suspend fun getMovies(movieId: Long): ApiResult<GetMovieResponse?>
}