package com.jukti.bookbrowser.di

import android.content.Context
import androidx.room.Room
import com.jukti.bookbrowser.data.local.BookDatabase
import com.jukti.bookbrowser.data.local.AuthorListConverter
import kotlinx.serialization.json.Json
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDatabase{
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "book_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookDao(bookDatabase: BookDatabase) = bookDatabase.bookDao()
}