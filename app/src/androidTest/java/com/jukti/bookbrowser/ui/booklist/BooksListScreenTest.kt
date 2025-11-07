package com.jukti.bookbrowser.ui.booklist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.jukti.bookbrowser.MainActivity
import com.jukti.bookbrowser.di.DataModule
import com.jukti.bookbrowser.fakes.FakeBookRepository
import com.jukti.bookbrowser.util.TestConstants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DataModule::class)
class BooksListScreenTest {

    @Inject
    lateinit var fakeBookRepository: FakeBookRepository

    @get: Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp() {
        hiltRule.inject()
    }


    @Test
    fun listScreen_showsLoading_thenBookList() {
        // Loading should be visible (fake is held)
        composeTestRule.onNodeWithTag(TestConstants.LOADING_INDICATOR_TEST_TAG).assertIsDisplayed()

        // Release fake and wait for Compose to settle
        fakeBookRepository.releaseEmission()
        composeTestRule.waitForIdle()

        // Verify final list
        composeTestRule.onNodeWithTag(TestConstants.LOADING_INDICATOR_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestConstants.BOOK_LIST_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithText("Dune").assertIsDisplayed()
        composeTestRule.onNodeWithText("Neuromancer").assertIsDisplayed()
    }


    @Test
    fun show_error_screen(){
        fakeBookRepository.shouldReturnError = true
        composeTestRule.onNodeWithTag(TestConstants.LOADING_INDICATOR_TEST_TAG).assertIsDisplayed()

        fakeBookRepository.releaseEmission()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TestConstants.ERROR_CONTENT_TEST_TAG).assertIsDisplayed()
    }



}