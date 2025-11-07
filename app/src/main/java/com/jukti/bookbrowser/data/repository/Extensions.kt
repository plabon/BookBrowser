package com.jukti.bookbrowser.data.repository

import com.jukti.bookbrowser.data.local.entities.AuthorEntity
import com.jukti.bookbrowser.data.local.entities.BookEntity
import com.jukti.bookbrowser.data.remote.dto.AuthorDto
import com.jukti.bookbrowser.data.remote.dto.BookDto
import com.jukti.bookbrowser.domain.model.Author
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.util.ImageUtils

fun BookDto.toEntity(): BookEntity =
    BookEntity(
        key = this.key,
        title = this.title,
        coverId = this.coverId,
        authors = this.authors.map { it.toEntity() }
    )

fun AuthorDto.toEntity(): AuthorEntity =
    AuthorEntity(
        key = this.key,
        name = this.name
    )

fun BookEntity.toDomain(): Book =
    Book(
        title = this.title,
        author = this.authors.map { Author(id = it.key, name = it.name) },
        coverImageUrl = ImageUtils.coverUrl(this.coverId)
    )

fun AuthorDto.toDomainModel(): Author {
    return Author(
        id = key,
        name = name
    )
}

fun BookDto.toDomainModel(): Book{
    return Book(
        title = title,
        author = authors.map { it.toDomainModel() },
        coverImageUrl = ImageUtils.coverUrl(coverId)
    )
}