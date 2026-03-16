package com.production.noteflow.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.app.navigation.Routes
import com.production.noteflow.domain.usecase.note.DeleteNoteUseCase
import com.production.noteflow.domain.usecase.note.GetNoteDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getNoteDetailUseCase: GetNoteDetailUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val noteId: String = checkNotNull(savedStateHandle[Routes.NOTE_ID])

    val uiState: StateFlow<NoteDetailUiState> = getNoteDetailUseCase(noteId)
        .map { detail ->
            NoteDetailUiState(
                isLoading = false,
                note = detail.note,
                reminders = detail.reminders
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NoteDetailUiState()
        )

    fun deleteCurrentNote(onDeleted: () -> Unit) {
        val note = uiState.value.note ?: return

        viewModelScope.launch {
            deleteNoteUseCase(note)
                .onSuccess { onDeleted() }
        }
    }
}