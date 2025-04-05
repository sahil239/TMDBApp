package dev.sahildesai.tmdbapp.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.data.util.ApiResult
import dev.sahildesai.tmdbapp.domain.IGetMovieDetailsUseCase
import dev.sahildesai.tmdbapp.domain.IGetSimilarMoviesUseCase
import dev.sahildesai.tmdbapp.ui.navigation.MovieDetails
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MovieDetailState {
    data object Loading : MovieDetailState()
    data class Success(val movie: Movie, val similarMovies: List<Movie> = emptyList()) : MovieDetailState()
    data class Error(val message: String) : MovieDetailState()
}

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getMovieDetailsUseCase: IGetMovieDetailsUseCase,
    getSimilarMoviesUseCase: IGetSimilarMoviesUseCase
): ViewModel() {

    private val _movieDetailState = MutableStateFlow<MovieDetailState>(MovieDetailState.Loading)
    val movieDetailState: StateFlow<MovieDetailState> = _movieDetailState

    init {
        val movieId = savedStateHandle.toRoute<MovieDetails>().id

        viewModelScope.launch {

            val detailsDeferred = async { getMovieDetailsUseCase.getMovie(movieId) }
            val similarMoviesDeferred = async { getSimilarMoviesUseCase.getMovies(movieId) }

            val detailsResult = detailsDeferred.await()

            if (detailsResult is ApiResult.Success) {
                _movieDetailState.value = MovieDetailState.Success(
                    movie = detailsResult.data,
                    similarMovies = when(val similarMoviesResult = similarMoviesDeferred.await()){
                        is ApiResult.Success -> similarMoviesResult.data?.movies?.sortedByDescending { it.voteAvg }?.take(9) ?: emptyList()
                        is ApiResult.Failure -> emptyList()
                    }
                )
            } else {
                _movieDetailState.value = MovieDetailState.Error((detailsResult as ApiResult.Failure).errorMessage)
            }
        }
    }
}