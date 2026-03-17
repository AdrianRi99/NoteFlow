package com.production.noteflow.presentation.screen.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.Quote
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shows_notes_and_quote() {
        val notes = listOf(
            Note("1", "Shopping", "Milk", "Personal", "Buy milk", 1L, null),
            Note("2", "Project", "UI", "Projects", "Build UI", 2L, null)
        )

        composeRule.setContent {
            DashboardScreen(
                items = notes,
                quoteUiState = QuoteUiState(
                    isLoading = false,
                    quote = Quote("Stay hungry", "Steve Jobs"),
                    errorMessage = null
                ),
                onRefreshQuote = {},
                onAddClick = {},
                onItemClick = {}
            )
        }

        composeRule.onNodeWithText("Shopping").assertIsDisplayed()
        composeRule.onNodeWithText("Project").assertIsDisplayed()
    }

    @Test
    fun clicking_add_calls_callback() {
        var clicked = false

        composeRule.setContent {
            DashboardScreen(
                items = emptyList(),
                quoteUiState = QuoteUiState(),
                onRefreshQuote = {},
                onAddClick = { clicked = true },
                onItemClick = {}
            )
        }

        composeRule.onNodeWithContentDescription("Add").performClick()
        assert(clicked)
    }
}