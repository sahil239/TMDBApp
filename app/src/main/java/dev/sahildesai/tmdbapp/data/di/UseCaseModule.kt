package dev.sahildesai.tmdbapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sahildesai.tmdbapp.data.repository.IMoviesRepository
import dev.sahildesai.tmdbapp.domain.GetMovieDetailsUseCase
import dev.sahildesai.tmdbapp.domain.GetMoviesUseCase
import dev.sahildesai.tmdbapp.domain.GetSimilarMoviesUseCase
import dev.sahildesai.tmdbapp.domain.IGetMovieDetailsUseCase
import dev.sahildesai.tmdbapp.domain.IGetMoviesUseCase
import dev.sahildesai.tmdbapp.domain.IGetSimilarMoviesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun providesGetMoviesUseCase(
        movieRepository: IMoviesRepository
    ): IGetMoviesUseCase = GetMoviesUseCase(movieRepository)

    @Singleton
    @Provides
    fun providesGetMovieDetailsUseCase(
        movieRepository: IMoviesRepository
    ): IGetMovieDetailsUseCase = GetMovieDetailsUseCase(movieRepository)

    @Singleton
    @Provides
    fun providesGetSimilarMoviesUseCase(
        movieRepository: IMoviesRepository
    ): IGetSimilarMoviesUseCase = GetSimilarMoviesUseCase(movieRepository)

}