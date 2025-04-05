package dev.sahildesai.tmdbapp.ui.list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dev.sahildesai.tmdbapp.data.api.GetMovieResponse
import dev.sahildesai.tmdbapp.data.makeMovie
import dev.sahildesai.tmdbapp.data.util.ApiResult
import dev.sahildesai.tmdbapp.domain.IGetMovieDetailsUseCase
import dev.sahildesai.tmdbapp.domain.IGetSimilarMoviesUseCase
import dev.sahildesai.tmdbapp.ui.details.MovieDetailState
import dev.sahildesai.tmdbapp.ui.details.MovieDetailsViewModel
import dev.sahildesai.tmdbapp.ui.navigation.MovieDetails
import dev.sahildesai.tmdbapp.utils.SavedStateHandleRule
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MoviesDetailsViewModelTest {

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(MovieDetails(id=1L))

    private val getMovieDetailsUseCase: IGetMovieDetailsUseCase = mockk(relaxed = true){
        coEvery { getMovie(any()) } returns ApiResult.Success(makeMovie(id = 1L))
    }

    private val similarMoviesUseCase: IGetSimilarMoviesUseCase = mockk(relaxed = true){
        coEvery { getMovies(any()) } returns ApiResult.Success(
            GetMovieResponse(
                page = 1,
                totalResult = 5,
                totalPages = 2,
                movies = listOf( makeMovie(id = 2L))
            )
        )
    }

    private val testDispatcher = StandardTestDispatcher()

    private fun createViewModel(savedStateHandle: SavedStateHandle): MovieDetailsViewModel = MovieDetailsViewModel(
        savedStateHandle,
        getMovieDetailsUseCase,
        similarMoviesUseCase
    )

    @Test
    fun `ui state when both api returns Success`() = runTest(testDispatcher) {

        coEvery { getMovieDetailsUseCase.getMovie(any()) } returns ApiResult.Success(makeMovie(id = 1L))
        coEvery { similarMoviesUseCase.getMovies(any()) } returns ApiResult.Success(
            GetMovieResponse(
                page = 1,
                totalResult = 5,
                totalPages = 2,
                movies = listOf( makeMovie(id = 2L))
            )
        )

        val viewModel = createViewModel(savedStateHandleRule.savedStateHandleMock)
        viewModel.movieDetailState.value shouldBe MovieDetailState.Loading

        viewModel.movieDetailState.test {
            awaitItem()
            val data = viewModel.movieDetailState.value as MovieDetailState.Success
            data.movie shouldBe makeMovie(id = 1L)
            data.similarMovies shouldBe listOf( makeMovie(id = 2L))
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `ui state when details is failure but similar movies is success`() = runTest(testDispatcher) {

        coEvery { getMovieDetailsUseCase.getMovie(any()) } returns ApiResult.Failure(1, "Error")
        coEvery { similarMoviesUseCase.getMovies(any()) } returns ApiResult.Success(
            GetMovieResponse(
                page = 1,
                totalResult = 5,
                totalPages = 2,
                movies = listOf( makeMovie(id = 2L))
            )
        )

        val viewModel = createViewModel(savedStateHandleRule.savedStateHandleMock)

        viewModel.movieDetailState.test {
            awaitItem()
            val data = viewModel.movieDetailState.value as MovieDetailState.Error
            data.message shouldBe "Error"
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ui state when details is success but similar movies is failure`() = runTest(testDispatcher) {

        coEvery { getMovieDetailsUseCase.getMovie(any()) } returns ApiResult.Success(makeMovie(1L))
        coEvery { similarMoviesUseCase.getMovies(any()) } returns ApiResult.Failure(1, "Error")

        val viewModel = createViewModel(savedStateHandleRule.savedStateHandleMock)

        viewModel.movieDetailState.value shouldBe MovieDetailState.Loading

        viewModel.movieDetailState.test {
            awaitItem()
            val data = viewModel.movieDetailState.value as MovieDetailState.Success
            data.movie shouldBe makeMovie(1L)
            data.similarMovies shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ui state when both api returns failure`() = runTest(testDispatcher) {
        coEvery { getMovieDetailsUseCase.getMovie(any()) } returns ApiResult.Failure(1, "Error fetching details.")
        coEvery { similarMoviesUseCase.getMovies(any()) } returns ApiResult.Failure(2, "Error fetching similar movies")

        val viewModel = createViewModel(savedStateHandleRule.savedStateHandleMock)
        viewModel.movieDetailState.value shouldBe MovieDetailState.Loading


        viewModel.movieDetailState.test {
            awaitItem()
            val data = viewModel.movieDetailState.value as MovieDetailState.Error
            data.message shouldBe "Error fetching details."
            cancelAndIgnoreRemainingEvents()
        }
    }

}
