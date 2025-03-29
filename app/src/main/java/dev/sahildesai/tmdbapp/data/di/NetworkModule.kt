package dev.sahildesai.tmdbapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sahildesai.tmdbapp.data.api.APIService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient{
        // Interceptor to add custom headers to each request.
        val headerInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestWithHeaders = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxZWUzMTQ4ZDg4OTc2NmY3YTBjNThlZmRmMTQxZTA3ZSIsIm5iZiI6MTc0Mjg5Njc2NC41NDQ5OTk4LCJzdWIiOiI2N2UyN2U3YzRjNTI3NDY2NjVkYzg1ODciLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.-86ay-MjvIVhmYUEY4Gqz_r7Jpy8v2AEMnk3T_GT-5U")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(requestWithHeaders)
        }

        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): APIService = retrofit.create(APIService::class.java)
}