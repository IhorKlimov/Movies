package com.example.movies

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.text.TextLayoutResult
import com.example.movies.ui.screens.home.HomeScreen
import com.example.movies.ui.screens.home.HomeViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class HomeScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Mock
    lateinit var viewModel: HomeViewModel

    @Test
    fun init() {
        whenever(viewModel.isLoading).doReturn(true)
        whenever(viewModel.movies).doReturn(mutableStateListOf())

        rule.setContent {
            HomeScreen(
                {},
                viewModel
            )
        }

        rule.onRoot(true).printToLog("verseion5log")
        rule.onNode(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)
        ).assertIsDisplayed()

        rule.onNodeWithText("Movies")
            .assertIsDisplayed()
            .assertWithColor(Color.Red)
    }
}

private fun SemanticsNodeInteraction.assertWithColor(color: Color): SemanticsNodeInteraction {
    val matcher = SemanticsMatcher("Color matcher") {
        val list = mutableStateListOf<TextLayoutResult>()
        it.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(list)
        val currentColor = list.firstOrNull()?.layoutInput?.style?.color
        currentColor == color
    }

    return this.assert(matcher)
}

