package com.jukti.bookbrowser.fakes

import android.util.Log
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Resource
import com.jukti.bookbrowser.domain.model.Author
import com.jukti.bookbrowser.domain.repository.BookRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.concurrent.Volatile

private const val TAG = "FakeBookRepository"

class FakeBookRepository @Inject constructor() : BookRepository {

    var delayInMillis = 0L
    var shouldReturnError = false

    @Volatile
    private var emissionController : CompletableDeferred<Unit>? = null

    fun holdEmission(){
        Log.d(TAG, "holdEmission() called - creating gate and holding emissions")
        emissionController = CompletableDeferred()
    }

    fun releaseEmission(){
        Log.d(TAG, "releaseEmission() called - releasing gate if present")
        emissionController?.complete(Unit)
        emissionController = null
    }

    override fun getScienceFictionBooks(): Flow<Resource<List<Book>>> {
        return flow {
            // emit loading first
            emit(Resource.Loading)

            // suspend until the test releases the gate
            emissionController?.await()

            if(delayInMillis>0){
                delay(delayInMillis)
            }

            if (shouldReturnError) {
                emit(Resource.Error(message = "Failed to load books"))
            } else {
                val sampleBooks = listOf(
                    Book(
                        title = "Dune",
                        author = listOf(Author(id = "a1", name = "Plabon")),
                        coverImageUrl = null
                    ),
                    Book(
                        title = "Neuromancer",
                        author = listOf(Author(id = "a2", name = "modak")),
                        coverImageUrl = null
                    )
                )

                emit(Resource.Success(sampleBooks))
            }
        }
    }

}