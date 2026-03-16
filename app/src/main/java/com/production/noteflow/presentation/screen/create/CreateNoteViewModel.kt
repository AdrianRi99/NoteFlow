package com.production.noteflow.presentation.screen.create

import androidx.lifecycle.viewModelScope
import com.production.noteflow.domain.usecase.note.CreateNoteUseCase
import com.production.noteflow.presentation.common.BaseNoteEditorViewModel
import com.production.noteflow.presentation.model.NoteEditorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase
) : BaseNoteEditorViewModel() {

    fun saveNote() {
        val state = uiState.value

        viewModelScope.launch {
            createNoteUseCase(
                title = state.title,
                subtitle = state.subtitle,
                content = state.content,
                tag = state.selectedTag,
                imageUri = state.selectedImageUri,
                reminders = state.reminders
            ).onSuccess {
                sendEvent(NoteEditorEvent.Saved)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Speichern fehlgeschlagen."
                setError(message)
                sendEvent(NoteEditorEvent.ShowError(message))
            }
        }
    }
}