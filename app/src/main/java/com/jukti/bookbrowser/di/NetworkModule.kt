package com.jukti.bookbrowser.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jukti.bookbrowser.data.remote.BookApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    @Suppress("unused")
    @Named("baseUrl")
    fun provideBaseUrl(): String = "https://openlibrary.org/"

    @Provides
    @Singleton
    fun provideJson(): Json{
        return Json{
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .also { client ->
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                client.addInterceptor(loggingInterceptor)
            }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, json: Json, @Named("baseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory(contentType = contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit): BookApiService {
        return retrofit.create(BookApiService::class.java)
    }


}