package com.jukti.bookbrowser.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val key:String,
    val title: String,
    val coverId: Int? = null,
    val authors: List<AuthorEntity>
)

@Serializable
data class AuthorEntity(
    val key: String,
    val name: String
)
