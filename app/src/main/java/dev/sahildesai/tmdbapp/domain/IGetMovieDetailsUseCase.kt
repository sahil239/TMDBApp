package dev.sahildesai.tmdbapp.domain

import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.data.util.ApiResult

interface IGetMovieDetailsUseCase {
    suspend fun getMovie(id: Long): ApiResult<Movie>
}