package dev.sahildesai.tmdbapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sahildesai.tmdbapp.data.api.APIService
import dev.sahildesai.tmdbapp.data.repository.impl.MoviesRepository
import dev.sahildesai.tmdbapp.data.repository.IMoviesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGetMovies(apiService: APIService): IMoviesRepository = MoviesRepository(apiService)
}