package com.jukti.bookbrowser.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookResponseDto(
    @SerialName("works")
    val books: List<BookDto>
)
