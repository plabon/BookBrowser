package com.jukti.bookbrowser.ui.booklist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.jukti.bookbrowser.R
import com.jukti.bookbrowser.util.TestConstants

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag(TestConstants.ERROR_CONTENT_TEST_TAG),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = stringResource(id = R.string.error_generic),
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.error_generic),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ErrorScreen()
}
