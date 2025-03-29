package dev.sahildesai.tmdbapp.ui.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MovieDetailState {
    data object Loading : MovieDetailState()
    data class Success(val movie: Movie, val similarMovies: List<Movie>? = null) : MovieDetailState()
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


    var movie by mutableStateOf<Movie?>(null)
        private set


    init {
        val movieId = savedStateHandle.toRoute<MovieDetails>().id

        viewModelScope.launch {

            delay(5000L)
            val similarMoviesResult = getSimilarMoviesUseCase.getMovies(movieId)
            val similarMovies = when(similarMoviesResult){
                is ApiResult.Success -> similarMoviesResult.data?.movies?.sortedByDescending { it.voteAvg }?.take(9) ?: emptyList()
                is ApiResult.Failure -> emptyList()
            }

            val result = getMovieDetailsUseCase.getMovie(movieId)

            _movieDetailState.value = when(result){
                is ApiResult.Success -> MovieDetailState.Success(result.data, similarMovies = similarMovies)
                is ApiResult.Failure -> MovieDetailState.Error(result.errorMessage)
            }

        }
    }
}