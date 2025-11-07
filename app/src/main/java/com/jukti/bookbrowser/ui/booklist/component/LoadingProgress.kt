package com.jukti.bookbrowser.ui.booklist.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.jukti.bookbrowser.R
import com.jukti.bookbrowser.util.TestConstants

@Composable
fun LoadingProgress(
    modifier: Modifier = Modifier,
) {
    val message = stringResource(id = R.string.loading_books)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag(TestConstants.LOADING_INDICATOR_TEST_TAG),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingProgressPreview() {
    LoadingProgress()
}
