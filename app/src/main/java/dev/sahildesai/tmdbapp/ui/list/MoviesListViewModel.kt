package dev.sahildesai.tmdbapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.domain.IGetMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MoviesState {
    data object Loading : MoviesState()
    data class Success(val movies: Flow<PagingData<Movie>>) : MoviesState()
    data class Error(val message: String) : MoviesState()
}

@HiltViewModel
class MoviesListViewModel@Inject constructor(
    private val getMoviesUseCase: IGetMoviesUseCase
): ViewModel() {

    private val _moviesState = MutableStateFlow<MoviesState>(MoviesState.Loading)
    val moviesState: StateFlow<MoviesState> = _moviesState

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        _moviesState.value = MoviesState.Loading
        viewModelScope.launch {
            try {
                _moviesState.value = MoviesState.Success(getMoviesUseCase.getMovies().cachedIn(viewModelScope))
            } catch (e: Exception) {
                _moviesState.value = MoviesState.Error("Failed to load movies: ${e.message}")
            }
        }
    }

    // Retry the movie fetch in case of failure
    fun retryFetchMovies() {
        fetchMovies()
    }
}