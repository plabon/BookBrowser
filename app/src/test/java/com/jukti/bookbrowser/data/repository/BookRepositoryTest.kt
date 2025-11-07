package com.jukti.bookbrowser.data.repository

import com.jukti.bookbrowser.data.local.dao.BookDao
import com.jukti.bookbrowser.data.local.entities.AuthorEntity
import com.jukti.bookbrowser.data.local.entities.BookEntity
import com.jukti.bookbrowser.data.remote.BookApiService
import com.jukti.bookbrowser.data.remote.dto.AuthorDto
import com.jukti.bookbrowser.data.remote.dto.BookDto
import com.jukti.bookbrowser.data.remote.dto.BookResponseDto
import com.jukti.bookbrowser.domain.model.Resource
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Author
import com.jukti.bookbrowser.util.ImageUtils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.jukti.bookbrowser.domain.repository.BookRepository

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryTest {

    @MockK
    private lateinit var bookApiService: BookApiService

    @MockK
    private lateinit var bookDao: BookDao

    private lateinit var bookRepository: BookRepository

    private val scheduler = TestCoroutineScheduler()

    private val mockDispatcher = StandardTestDispatcher(scheduler)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bookRepository = BookRepositoryImpl(bookApiService, bookDao, mockDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getScienceFictionBooks - success caches data and emits Success`() = runTest(scheduler) {

        val authorDto = AuthorDto(key = "A1", name = "Author 1")
        val bookDto = BookDto(key = "B1", title = "Title 1", authors = listOf(authorDto), coverId = 10)
        val expectedBook = Book(
            title = "Title 1",
            author = listOf(Author(id = "A1", name = "Author 1")),
            coverImageUrl = ImageUtils.coverUrl(10)
        )
        val response = BookResponseDto(books = listOf(bookDto))

        coEvery { bookApiService.getScienceFictionBooks() } returns response
        coEvery { bookDao.clearAll() } returns Unit
        coEvery { bookDao.insertBooks(any()) } returns Unit


        bookRepository.getScienceFictionBooks().test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(Resource.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(Resource.Success::class.java)


            val expected = Resource.Success(listOf(expectedBook))

            assertThat(second).isEqualTo(expected)

            // Verify DB interactions
            coVerify(exactly = 1) { bookDao.clearAll() }
            coVerify(exactly = 1) { bookDao.insertBooks(match { it.size == 1 }) }


            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getScienceFictionBooks - network failure uses cached data`() = runTest(scheduler) {

        coEvery { bookApiService.getScienceFictionBooks() } throws RuntimeException("network down")

        val cachedAuthor = AuthorEntity(key = "A1", name = "Cached Author")
        val cachedBook = BookEntity(key = "B1", title = "Cached Title", coverId = 5, authors = listOf(cachedAuthor))
        coEvery { bookDao.getAllBooks() } returns listOf(cachedBook)


        bookRepository.getScienceFictionBooks().test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(Resource.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(Resource.Success::class.java)
            val success = second as Resource.Success
            // Build expected domain object from cached entity and compare Resource equality
            val expectedBook = Book(
                title = "Cached Title",
                author = listOf(Author(id = "A1", name = "Cached Author")),
                coverImageUrl = ImageUtils.coverUrl(5)
            )
            val expected = Resource.Success(listOf(expectedBook))
            assertThat(second).isEqualTo(expected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getScienceFictionBooks - network failure and empty cache emits Error`() = runTest(scheduler) {

        coEvery { bookApiService.getScienceFictionBooks() } throws RuntimeException("network down")
        coEvery { bookDao.getAllBooks() } returns emptyList()

        bookRepository.getScienceFictionBooks().test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(Resource.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(Resource.Error::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }
}