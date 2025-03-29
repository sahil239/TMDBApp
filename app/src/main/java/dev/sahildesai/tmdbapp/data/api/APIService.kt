package dev.sahildesai.tmdbapp.data.api

import dev.sahildesai.tmdbapp.data.util.ApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("discover/movie")
    suspend fun getMovies(@Query("with_people")people: String, @Query("page") page: Int): Response<GetMovieResponse>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Long):Response<Movie>

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovies(@Path("movieId") movieId: Long): Response<GetMovieResponse>
}