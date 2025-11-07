package com.jukti.bookbrowser.ui.booklist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.jukti.bookbrowser.R
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jukti.bookbrowser.domain.model.Book
import com.jukti.bookbrowser.domain.model.Author

@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
) {
    val authorsText =
        if (book.author.isNotEmpty()) book.author.joinToString(", ") { it.name } else "Unknown author"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current
                )
                    .data(book.coverImageUrl)
                    .crossfade(300)
                    .build(),
                contentDescription = if (!book.coverImageUrl.isNullOrBlank()) "Cover for ${book.title} by $authorsText" else null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(id = R.drawable.book_cover_placeholder),
                error = painterResource(id = R.drawable.book_cover_error),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Title and author (right)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = authorsText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    val sampleBook = Book(
        title = "Dune",
        author = listOf(Author(id = "1", name = "Frank Herbert")),
        coverImageUrl = null
    )

    MaterialTheme {
        Surface {
            BookCard(
                book = sampleBook,
            )
        }
    }
}