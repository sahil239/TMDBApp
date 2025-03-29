package dev.sahildesai.tmdbapp.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.formatDate
import dev.sahildesai.tmdbapp.ui.navigation.MovieDetails
import dev.sahildesai.tmdbapp.ui.widgets.ErrorData
import dev.sahildesai.tmdbapp.ui.widgets.LoadImageFromUrl
import dev.sahildesai.tmdbapp.ui.widgets.LoadingData
import dev.sahildesai.tmdbapp.ui.widgets.ShowError
import kotlinx.coroutines.flow.collectLatest
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MoviesListViewModel = hiltViewModel()
){
    Column (modifier = Modifier.fillMaxSize().padding(top = 48.dp)){
        Text(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            text = "Cumberbatch's Movies",
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )

    when(val moviesState = viewModel.moviesState.collectAsState().value){
        is MoviesState.Loading -> LoadingData()
        is MoviesState.Error -> ShowError(
            ErrorData(
                message = "Error loading data: ${moviesState.message}",
                buttonText = "Retry",
                action = viewModel::retryFetchMovies)
        )
        is MoviesState.Success -> {
            val lazyPagingItems = moviesState.movies.collectAsLazyPagingItems()
            ShowMovieList(lazyPagingItems ,navController::navigate ,viewModel::retryFetchMovies)
        }
        }
    }

}

@Composable
fun ShowMovieList(lazyPagingItems: LazyPagingItems<Movie>, onSimilarMovieClick: (MovieDetails) -> Unit, onRetryClicked: () -> Unit) {
    val lazyListState = rememberLazyListState() // Remember the scroll state

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }.collectLatest {  }

    }
    LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState) {
        items(lazyPagingItems) { movie ->
            movie?.let { MovieRow(movie = movie) { onSimilarMovieClick(MovieDetails(movie.id)) } }
        }
        // Handling LoadState (when loading more items)
        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.refresh is LoadState.Error ||  loadState.append is LoadState.Error -> {
                    item {
                        val message = if(loadState.refresh is LoadState.Error){
                            val loadStateError = loadState.refresh as LoadState.Error
                            loadStateError.error.message
                        }else {
                            val loadStateError = loadState.append as LoadState.Error
                            loadStateError.error.message
                        } ?: "Unknown Error."
                        ShowError(
                            ErrorData(
                                message = message,
                                buttonText = "Retry",
                                action = onRetryClicked
                            )
                        )
                    }
                }
            }

            // Display loading indicator when appending data
            if (loadState.append is LoadState.NotLoading
                && !loadState.append.endOfPaginationReached
                && loadState.refresh !is LoadState.Loading
                && loadState.append !is LoadState.Error
                && loadState.refresh !is LoadState.Error
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                item {
                    Text(
                        text = "End of Data",
                        modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie, onClick : () -> Unit) {
    // Card for the row, with rounded corners
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Movie Poster Image
            val imageUrl = "https://image.tmdb.org/t/p/w500/${movie.posterPath}"
            LoadImageFromUrl(
                title = movie.title,
                imageUrl = imageUrl,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp)) // Spacer between image and text

            // Movie Title and Other Details
            Column(
                modifier = Modifier.weight(1f) // Takes up the remaining space
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // To truncate long titles
                )

                val dec = DecimalFormat("#.#")
                val date = if(movie.releaseDate.isBlank()) "N/A" else formatDate(LocalDate.parse(movie.releaseDate))
                // Movie release year and rating
                Text(
                    text = "Release: $date Rating: ${ dec.format(movie.voteAvg)}", // Show release year
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}