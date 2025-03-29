package dev.sahildesai.tmdbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sahildesai.tmdbapp.ui.navigation.NavigationGraph
import dev.sahildesai.tmdbapp.ui.theme.TMDBAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMDBAppTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }
    }
}
