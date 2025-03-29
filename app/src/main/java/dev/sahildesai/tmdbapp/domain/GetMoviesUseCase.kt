package dev.sahildesai.tmdbapp.domain

import androidx.paging.PagingData
import dev.sahildesai.tmdbapp.data.repository.IMoviesRepository
import dev.sahildesai.tmdbapp.data.api.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: IMoviesRepository
) : IGetMoviesUseCase{
    override fun getMovies(): Flow<PagingData<Movie>> {
        return moviesRepository.getMovies()
    }
}