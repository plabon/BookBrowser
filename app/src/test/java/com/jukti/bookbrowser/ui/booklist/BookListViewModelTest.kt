package com.jukti.bookbrowser.ui.booklist

import app.cash.turbine.test
import com.jukti.bookbrowser.domain.usecases.GetScienceFictionBooksUseCase
import com.jukti.bookbrowser.domain.model.Author
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class BookListViewModelTest {

    @MockK
    private lateinit var getScienceFictionBooksUseCase: GetScienceFictionBooksUseCase

    private lateinit var viewModel: BookListViewModel

    private val scheduler = TestCoroutineScheduler()

    private val mockDispatcher = StandardTestDispatcher(scheduler)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mockDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when usecase emits Success viewModel emits Loaded state with books`() = runTest {
        //setup
        val expectedBook = Book(title = "Dune", author = listOf(Author(id = "a1", name = "Frank Herbert")), coverImageUrl = null)
        coEvery { getScienceFictionBooksUseCase() } returns flowOf(Resource.Success(listOf(expectedBook)))

        viewModel = BookListViewModel(getScienceFictionBooksUseCase)

        // Act & Assert
        viewModel.uiState.test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(BookListUiState.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(BookListUiState.Loaded::class.java)
            val loaded = second as BookListUiState.Loaded
            assertThat(loaded.books).isEqualTo(listOf(expectedBook))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial uiState is Loading before usecase emits`() = runTest {
        // setup
        coEvery { getScienceFictionBooksUseCase() } returns emptyFlow()

        viewModel = BookListViewModel(getScienceFictionBooksUseCase)

        // Assert
        viewModel.uiState.test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(BookListUiState.Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when usecase emits Error viewModel emits Error state with message`() = runTest {
        // setup
        coEvery { getScienceFictionBooksUseCase() } returns flowOf(Resource.Error<List<Book>>(message = "boom"))

        viewModel = BookListViewModel(getScienceFictionBooksUseCase)

        // Act & Assert
        viewModel.uiState.test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(BookListUiState.Loading::class.java)


            val second = awaitItem()
            assertThat(second).isInstanceOf(BookListUiState.Error::class.java)
            val errorState = second as BookListUiState.Error
            assertThat(errorState.message).isEqualTo("boom")

            cancelAndIgnoreRemainingEvents()
        }
    }
}