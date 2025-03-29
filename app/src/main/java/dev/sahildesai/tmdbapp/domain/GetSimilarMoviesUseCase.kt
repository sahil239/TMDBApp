package dev.sahildesai.tmdbapp.domain

import dev.sahildesai.tmdbapp.data.api.GetMovieResponse
import dev.sahildesai.tmdbapp.data.repository.IMoviesRepository
import dev.sahildesai.tmdbapp.data.util.ApiResult
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val moviesRepository: IMoviesRepository
) : IGetSimilarMoviesUseCase{

    override suspend fun getMovies(movieId: Long): ApiResult<GetMovieResponse>{
        return moviesRepository.getSimilarMovies(movieId)
    }
}