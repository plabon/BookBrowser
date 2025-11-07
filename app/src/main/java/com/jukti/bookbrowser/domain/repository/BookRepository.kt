package com.jukti.bookbrowser.domain.repository

import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getScienceFictionBooks(): Flow<Resource<List<Book>>>
}