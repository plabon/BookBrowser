package com.jukti.bookbrowser.data.remote

import com.jukti.bookbrowser.data.remote.dto.BookResponseDto
import retrofit2.http.GET


interface BookApiService {

    @GET("subjects/science_fiction.json?limit=200")
    suspend fun getScienceFictionBooks(): BookResponseDto

}