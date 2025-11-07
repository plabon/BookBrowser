package com.jukti.bookbrowser.ui.booklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jukti.bookbrowser.ui.booklist.component.BookList
import com.jukti.bookbrowser.ui.booklist.component.ErrorScreen
import com.jukti.bookbrowser.ui.booklist.component.LoadingProgress

@Composable
fun BookListScreen(
    modifier: Modifier,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp, vertical = 0.dp)) {
        when (val state = uiState) {
            is BookListUiState.Error -> ErrorScreen()
            is BookListUiState.Loaded -> BookList(state.books)
            BookListUiState.Loading -> LoadingProgress()
        }
    }

}