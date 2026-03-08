package com.production.noteflow.ui.screen.detail

import com.production.noteflow.data.local.NoteEntity

data class NoteDetailUiState(
    val isLoading: Boolean = true,
    val note: NoteEntity? = null
)