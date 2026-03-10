package com.production.noteflow.ui.screen.detail

import com.production.noteflow.data.local.entities.NoteEntity
import com.production.noteflow.data.local.entities.ReminderEntity

data class NoteDetailUiState(
    val isLoading: Boolean = true,
    val note: NoteEntity? = null,
    val reminders: List<ReminderEntity> = emptyList()
)