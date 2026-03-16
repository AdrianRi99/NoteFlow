package com.production.noteflow.presentation.model

sealed interface NoteEditorMode {
    data object Create : NoteEditorMode
    data object Edit : NoteEditorMode
}