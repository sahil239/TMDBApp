package dev.sahildesai.tmdbapp.domain

import androidx.paging.PagingData
import dev.sahildesai.tmdbapp.data.api.Movie
import kotlinx.coroutines.flow.Flow

interface IGetMoviesUseCase {
    fun getMovies(): Flow<PagingData<Movie>>
}