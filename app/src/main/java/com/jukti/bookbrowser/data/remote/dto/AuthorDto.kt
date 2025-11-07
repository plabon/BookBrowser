package com.jukti.bookbrowser.data.remote.dto

import com.jukti.bookbrowser.domain.model.Author
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val key: String,
    val name: String
)

