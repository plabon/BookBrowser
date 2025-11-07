package com.jukti.bookbrowser.data.repository

import com.jukti.bookbrowser.data.local.dao.BookDao
import com.jukti.bookbrowser.data.remote.BookApiService
import com.jukti.bookbrowser.di.IoDispatcher
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Resource
import com.jukti.bookbrowser.domain.repository.BookRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    val bookApiService: BookApiService,
    val bookDao: BookDao,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : BookRepository {
    override fun getScienceFictionBooks(): Flow<Resource<List<Book>>> {
        return flow {
            emit(Resource.Loading)
            try {
                val booksDomain = withContext(dispatcher) {
                    val response = bookApiService.getScienceFictionBooks()
                    val domain = response.books.map { it.toDomainModel() }
                    val entities = response.books.map { it.toEntity() }

                    bookDao.clearAll()
                    bookDao.insertBooks(entities)

                    domain
                }

                emit(Resource.Success(booksDomain))
            } catch (e: Exception) {
                emit(getCachedOrError(e))
            }
        }
    }

    suspend fun getCachedOrError(e: Exception): Resource<List<Book>> {
        val cached = bookDao.getAllBooks()
        if (cached.isNotEmpty()) {
            val cachedDomain = cached.map { it.toDomain() }
            return Resource.Success(cachedDomain)
        } else {
            return Resource.Error(message = e.message.toString())
        }
    }


}