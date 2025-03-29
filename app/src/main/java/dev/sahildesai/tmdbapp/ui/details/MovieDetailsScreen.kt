package dev.sahildesai.tmdbapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.sahildesai.tmdbapp.data.api.Movie
import dev.sahildesai.tmdbapp.ui.navigation.MovieDetails
import dev.sahildesai.tmdbapp.ui.widgets.ErrorData
import dev.sahildesai.tmdbapp.ui.widgets.LoadImageFromUrl
import dev.sahildesai.tmdbapp.ui.widgets.LoadingData
import dev.sahildesai.tmdbapp.ui.widgets.ShowError

@Composable
fun MovieDetailsScreen(
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()){

    when(val movieDetailState = viewModel.movieDetailState.collectAsState().value) {
        is MovieDetailState.Loading -> LoadingData()
        is MovieDetailState.Error -> {
            ShowError(
                ErrorData(
                    message = movieDetailState.message.ifEmpty { "Something went wrong" },
                    action = navController::popBackStack)
            )
        }
        is MovieDetailState.Success -> ShowMovieDetails(
            movieDetailState.movie,
            movieDetailState.similarMovies,
            navController::navigate
        ) { navController.popBackStack() }
    }
}

@Composable
fun ShowMovieDetails(movie: Movie, similarMovies: List<Movie>?, onSimilarMovieClicked: (MovieDetails)-> Unit, onBackPressed: ()->Unit){
    val connection = remember {
        object : NestedScrollConnection {
        }
    }
    Box(modifier = Modifier.padding(top = 24.dp)){

    Column(verticalArrangement = Arrangement.spacedBy(10.dp) , modifier = Modifier.nestedScroll(connection).verticalScroll(rememberScrollState())
    ) {
        LoadImageFromUrl(
            title = movie.title,
            imageUrl = "https://image.tmdb.org/t/p/w500/${movie.backdropPath}",
            modifier = Modifier.fillMaxWidth().height(250.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Text(text = movie.title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            val genres = movie.let {
                it.genres.joinToString(", ") { genres -> genres.name }
            }
            Text(
                text = "Genre: $genres",
                fontSize = 15.sp,
                textAlign = TextAlign.Justify
            )
            Text(text = movie.overview, fontSize = 18.sp, textAlign = TextAlign.Justify)
        }

           SimilarMoviesGrid(similarMovies, onSimilarMovieClicked)
    }
        IconButton(modifier = Modifier.padding(15.dp).size(30.dp).background(color = Color.Black, shape = CircleShape),
            onClick = { onBackPressed() }) {
            Icon(
                tint = Color.White,
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back"
            )
        }
    }

}

@Composable
fun SimilarMoviesGrid(similarMovies: List<Movie>?, onSimilarMovieClicked: (MovieDetails) -> Unit) {
    Column {
        if(!similarMovies.isNullOrEmpty()) {
            Text(
                color = Color.White,
                text = "More like this",
                modifier = Modifier.fillMaxWidth().background(Color.Black)
                    .padding(vertical = 5.dp, horizontal = 15.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (similarMovies.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(8.dp).height(450.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp,),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(similarMovies.size) { index ->
                        val similarMovie = similarMovies[index]
                        LoadImageFromUrl(
                            modifier = Modifier.clickable {
                                onSimilarMovieClicked(MovieDetails(similarMovie.id))
                            },
                            title = similarMovie.title,
                            imageUrl = "https://image.tmdb.org/t/p/w500/${similarMovie.posterPath}"
                        )
                    }
                }
            }
        }
    }
}



