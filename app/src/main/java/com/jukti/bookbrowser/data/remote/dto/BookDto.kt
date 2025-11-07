package com.jukti.bookbrowser.data.remote.dto

import com.jukti.bookbrowser.domain.model.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val key: String,
    val title: String,
    val authors: List<AuthorDto>,
    @SerialName("cover_id") val coverId: Int? = null
)


