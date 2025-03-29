package dev.sahildesai.tmdbapp.ui.list

import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import dev.sahildesai.tmdbapp.data.makeMovie
import dev.sahildesai.tmdbapp.domain.IGetMoviesUseCase
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MoviesListViewModelTest {

    private var mockGetMoviesUseCase: IGetMoviesUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private fun createViewModel(): MoviesListViewModel = MoviesListViewModel(mockGetMoviesUseCase)

    @Test
    fun `test fetch movies success`() = runTest(testDispatcher) {
        //successful response
        val mockPagingData = PagingData.from(listOf(makeMovie(1L)))
        val mockFLow = flowOf(mockPagingData)

        coEvery { mockGetMoviesUseCase.getMovies() } returns mockFLow// { emit(mockPagingData) }

        val viewModel = createViewModel()
        viewModel.moviesState.value shouldBe MoviesState.Loading
        testSuccess(viewModel)
    }

    @Test
    fun `test fetch movies error`() = runTest(testDispatcher) {
        coEvery { mockGetMoviesUseCase.getMovies() } throws Exception("Network error")
        val viewModel = createViewModel()

        viewModel.moviesState.value shouldBe MoviesState.Loading

        testError(viewModel)
    }

    @Test
    fun `test retry fetch movies after error`() = runTest(testDispatcher) {
        val mockPagingData = PagingData.from(listOf(makeMovie(id = 1L)))
        val mockFLow = flowOf(mockPagingData)
        coEvery { mockGetMoviesUseCase.getMovies() } throws Exception("Network error")

        val viewModel = createViewModel()

        viewModel.moviesState.value shouldBe MoviesState.Loading

        testError(viewModel)

        coEvery { mockGetMoviesUseCase.getMovies() } returns mockFLow

        viewModel.retryFetchMovies()
        testSuccess(viewModel)

    }

    private suspend fun testSuccess(viewModel: MoviesListViewModel) {
        viewModel.moviesState.test {

            val loadingState = awaitItem()
            (loadingState shouldBe MoviesState.Loading)

            // Now, verify the Success state with the correct PagingData
            val successState = awaitItem() // Will be the next state
            assert(successState is MoviesState.Success)
            val successData = successState as MoviesState.Success
            successData.movies.first().map { pagingData ->
                pagingData shouldBe makeMovie(1L)
            }

        }
    }

    private suspend fun testError(viewModel: MoviesListViewModel) {
        viewModel.moviesState.test {
            val errorState = awaitItem()
            errorState shouldBe  MoviesState.Error("Failed to load movies: Network error")
            val errorMessage = (errorState as MoviesState.Error).message
            assertEquals("Failed to load movies: Network error", errorMessage)
        }
    }
}