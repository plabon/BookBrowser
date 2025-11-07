package com.jukti.bookbrowser.domain.usecases

import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Resource
import com.jukti.bookbrowser.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScienceFictionBooksUseCase @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(): Flow<Resource<List<Book>>> {
        return bookRepository.getScienceFictionBooks()
    }
}