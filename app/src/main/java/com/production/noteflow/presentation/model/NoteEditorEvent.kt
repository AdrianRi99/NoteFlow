package com.production.noteflow.presentation.model

sealed interface NoteEditorEvent {
    data object Saved : NoteEditorEvent
    data object Deleted : NoteEditorEvent
    data class ShowError(val message: String) : NoteEditorEvent
}