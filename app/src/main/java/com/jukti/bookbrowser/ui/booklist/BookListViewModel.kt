package com.jukti.bookbrowser.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.usecases.GetScienceFictionBooksUseCase
import com.jukti.bookbrowser.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(val getScienceFictionBooksUseCase: GetScienceFictionBooksUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getScienceFictionBooksUseCase().collect { bookResource ->
                when (bookResource) {
                    is Resource.Loading -> _uiState.value = BookListUiState.Loading
                    is Resource.Success -> _uiState.value = BookListUiState.Loaded(bookResource.data)
                    is Resource.Error -> _uiState.value = BookListUiState.Error(bookResource.message)
                }
            }

        }
    }

}

sealed interface BookListUiState{
    object Loading : BookListUiState
    data class Loaded(val books: List<Book>) : BookListUiState
    data class Error(val message: String) : BookListUiState
}