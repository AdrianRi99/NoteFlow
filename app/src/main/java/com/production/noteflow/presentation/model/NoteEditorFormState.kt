package com.production.noteflow.presentation.model

import com.production.noteflow.domain.model.ReminderDraft
import com.production.noteflow.domain.model.ReminderDraftFactory

data class NoteEditorFormState(
    val title: String = "",
    val subtitle: String = "",
    val content: String = "",
    val selectedTag: String = "Ideas",
    val selectedImageUri: String? = null,
    val reminders: List<ReminderDraft> = ReminderDraftFactory.defaultReminderDrafts(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)