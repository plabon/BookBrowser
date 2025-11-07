package com.jukti.bookbrowser.di

import com.jukti.bookbrowser.data.repository.BookRepositoryImpl
import com.jukti.bookbrowser.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository

}