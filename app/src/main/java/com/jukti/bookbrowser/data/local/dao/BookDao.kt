package com.jukti.bookbrowser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jukti.bookbrowser.data.local.entities.BookEntity

@Dao
interface BookDao {

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books")
    suspend fun clearAll()
}