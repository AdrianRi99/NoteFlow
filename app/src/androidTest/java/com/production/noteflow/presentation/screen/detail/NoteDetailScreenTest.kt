package com.production.noteflow.presentation.screen.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.production.noteflow.domain.model.Note
import org.junit.Rule
import org.junit.Test

class NoteDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shows_note_content_when_note_exists() {
        composeRule.setContent {
            NoteDetailScreen(
                uiState = NoteDetailUiState(
                    isLoading = false,
                    note = Note(
                        id = "1",
                        title = "Meeting Notes",
                        subtitle = "Sprint planning",
                        tag = "Projects",
                        content = "Discuss backlog",
                        createdAt = 1L,
                        imageUri = null
                    ),
                    reminders = emptyList()
                ),
                onBack = {},
                onEdit = {},
                onDelete = {}
            )
        }

        composeRule.onNodeWithText("Meeting Notes").assertIsDisplayed()
        composeRule.onNodeWithText("Sprint planning").assertIsDisplayed()
        composeRule.onNodeWithText("Discuss backlog").assertIsDisplayed()
        composeRule.onNodeWithText("Projects").assertIsDisplayed()
    }

    @Test
    fun shows_not_found_message_when_note_is_null() {
        composeRule.setContent {
            NoteDetailScreen(
                uiState = NoteDetailUiState(
                    isLoading = false,
                    note = null,
                    reminders = emptyList()
                ),
                onBack = {},
                onEdit = {},
                onDelete = {}
            )
        }

        composeRule.onNodeWithText("This note does not exist").assertIsDisplayed()
    }
}