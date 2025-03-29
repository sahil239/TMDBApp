package dev.sahildesai.tmdbapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.sahildesai.tmdbapp.ui.details.MovieDetailsScreen
import dev.sahildesai.tmdbapp.ui.list.MovieListScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = MovieList) {
        composable<MovieList> {
            MovieListScreen(navController)
        }
        composable<MovieDetails> {
            MovieDetailsScreen(navController)
        }
    }
}

@Serializable
object MovieList

@Serializable
data class MovieDetails(val id: Long)
