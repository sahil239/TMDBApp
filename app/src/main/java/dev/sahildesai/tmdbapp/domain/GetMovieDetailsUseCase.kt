package dev.sahildesai.tmdbapp.domain

import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.data.repository.IMoviesRepository
import dev.sahildesai.tmdbapp.data.util.ApiResult
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: IMoviesRepository
) : IGetMovieDetailsUseCase{
    override suspend fun getMovie(id: Long): ApiResult<Movie>{
        return moviesRepository.getMovieDetails(id)
    }
}