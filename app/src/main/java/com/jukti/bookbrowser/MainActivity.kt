package com.jukti.bookbrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import com.jukti.bookbrowser.ui.booklist.BookListScreen
import com.jukti.bookbrowser.ui.theme.BookBrowserTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookBrowserTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Boring App") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }) { innerPadding ->
                    BookListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
