package com.jukti.bookbrowser.domain.model

data class Book(
    val title: String,
    val author: List<Author>,
    val coverImageUrl: String?
)
