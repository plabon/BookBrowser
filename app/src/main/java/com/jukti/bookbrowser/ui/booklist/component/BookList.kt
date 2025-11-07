package com.jukti.bookbrowser.ui.booklist.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.util.TestConstants

@Composable
fun BookList(bookList: List<Book>) {
    LazyColumn(modifier = Modifier
        .fillMaxHeight()
        .testTag(TestConstants.BOOK_LIST_TEST_TAG)) {
        items(bookList) { book ->
            BookCard(book = book)
        }
    }
}