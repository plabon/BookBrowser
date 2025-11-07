package com.jukti.bookbrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jukti.bookbrowser.data.local.dao.BookDao
import com.jukti.bookbrowser.data.local.entities.BookEntity

@Database(entities = [BookEntity::class], version = 1)
@TypeConverters(AuthorListConverter::class)
abstract class BookDatabase : RoomDatabase(){
    abstract fun bookDao(): BookDao
}
