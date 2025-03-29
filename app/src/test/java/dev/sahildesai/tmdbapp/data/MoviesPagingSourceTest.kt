package dev.sahildesai.tmdbapp.data

import android.util.Log
import androidx.paging.PagingSource
import dev.sahildesai.tmdbapp.data.api.APIService
import dev.sahildesai.tmdbapp.data.api.BelongsToCollection
import dev.sahildesai.tmdbapp.data.api.GetMovieResponse
import dev.sahildesai.tmdbapp.data.api.Movie
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import retrofit2.Response


/* Inspiration from:
https://github.com/android/architecture-components-samples/blob/master/PagingWithNetworkSample/app/src/test/java/com/android/example/paging/pagingwithnetwork/reddit/repository/SubredditPagingSourceTest.kt#L48
*/

class MoviesPagingSourceTest {

    private lateinit var apiService: APIService
    private lateinit var moviesPagingSource: MoviesPagingSource
    private val QUERY = "action"
    @Before
    fun setUp() {
        // Mock API service
        mockkStatic(Log::class)

        apiService = mockk()

        // Initialize MoviesPagingSource
        moviesPagingSource = MoviesPagingSource(apiService, QUERY)
    }

    @Test
    fun `test successful loading of data`() = runBlocking {
        // Mock the response from APIService
        val fakeMovieList = listOf(
            makeMovie(1),
            makeMovie(2)
        )

        val fakeResponse = Response.success(
            GetMovieResponse(
                page = 1,
                totalPages = 2,
                totalResult = 3,
                movies = fakeMovieList
            )
        )

        coEvery { apiService.getMovies(QUERY, 1) } returns fakeResponse

        // Call the load function on MoviesPagingSource
        val result = moviesPagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false)
        )

        // Assert that the result is a LoadResult.Page
        assertTrue(result is PagingSource.LoadResult.Page)

        // Verify the contents of the page
        val page = result as PagingSource.LoadResult.Page
        assertEquals(fakeMovieList.size, page.data.size)
        assertEquals("Inception-1", page.data[0].title)
        assertEquals("Inception-2", page.data[1].title)

        // Verify the next key and prev key
        assertEquals(2, page.nextKey)
        assertEquals(null, page.prevKey)
    }

    @Test
    fun `test loading data with error response`() = runBlocking {
        // Mock a failed response from the APIService
        val fakeErrorResponse = Response.error<GetMovieResponse>(400, "Bad Request".toResponseBody())

        coEvery { apiService.getMovies(QUERY, 1) } returns fakeErrorResponse

        // Call the load function on MoviesPagingSource
        val result = moviesPagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false)
        )

        // Assert that the result is a LoadResult.Error
        assertTrue(result is PagingSource.LoadResult.Error)

        val errorResult = result as PagingSource.LoadResult.Error
        assertTrue(errorResult.throwable is Exception)
    }

    @Test
    fun `test empty movie list`() = runBlocking {
        // Mock empty response
        val fakeEmptyResponse = Response.success(
            GetMovieResponse(
                page = 1,
                totalPages = 1,
                totalResult = 0,
                movies = emptyList()
            )
        )

        coEvery { apiService.getMovies(QUERY, 1) } returns fakeEmptyResponse

        // Call the load function on MoviesPagingSource
        val result = moviesPagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 2, placeholdersEnabled = false)
        )

        // Assert that the result is a LoadResult.Page
        assertTrue(result is PagingSource.LoadResult.Page)

        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data.isEmpty())
        assertEquals(null, page.nextKey)
        assertEquals(null, page.prevKey)
    }
}


fun makeMovie(id: Long = 1L) = Movie(
    id = id,
    title = "Inception-$id",
    originalLanguage = "en",
    overview = "LorenIpsum",
    posterPath = "",
    backdropPath = "",
    releaseDate = "",
    genreIds = emptyList(),
    genres = emptyList(),
    adult = false,
    video = false,
    voteAvg = 3f,
    originalTitle = "Inception-$id",
    voteCount = 10,
    budget = 50000,
    homePage = "",
    runTime = 150,
    belongsToCollection = BelongsToCollection(1, "", "", "")
)