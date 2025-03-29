package dev.sahildesai.tmdbapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.sahildesai.tmdbapp.data.api.APIService
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.data.util.ApiResult
import dev.sahildesai.tmdbapp.data.util.parseAPICall


class MoviesPagingSource(
    private val apiService: APIService,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return when(val response = parseAPICall { apiService.getMovies(query, page) }) {
                is ApiResult.Success -> {
                    val moviesResponse = response.data
                    if (moviesResponse.movies != null) {

                        val movies = moviesResponse.movies
                        val currentPage = moviesResponse.page
                        val totalPages = moviesResponse.totalPages

                        val nextKey = if (currentPage < totalPages) currentPage + 1 else null
                        val prevKey = if (currentPage == 1) null else currentPage - 1
                        LoadResult.Page(
                            data = movies,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )

                        }else {
                            LoadResult.Error(Exception("MOVIES NULL."))
                        }
                    }

                is ApiResult.Failure -> {
                    LoadResult.Error(Exception(response.errorMessage))
                }
            }



    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }
}