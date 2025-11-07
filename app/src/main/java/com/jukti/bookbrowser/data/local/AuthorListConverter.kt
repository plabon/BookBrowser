package com.jukti.bookbrowser.data.local

import androidx.room.TypeConverter
import com.jukti.bookbrowser.data.local.entities.AuthorEntity
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json


class AuthorListConverter() {

    val json = Json { ignoreUnknownKeys = true }


    @TypeConverter
    fun fromAuthorList(authors: List<AuthorEntity>): String {
        return json.encodeToString(ListSerializer(AuthorEntity.serializer()), authors)
    }

    @TypeConverter
    fun toAuthorsList(authorsString: String): List<AuthorEntity> {
        return json.decodeFromString(ListSerializer(AuthorEntity.serializer()), authorsString)
    }

}