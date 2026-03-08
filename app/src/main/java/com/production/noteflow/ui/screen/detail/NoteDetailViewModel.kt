package com.production.noteflow.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.ui.navigation.Routes
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
    private val repository: NoteRepository
) : ViewModel() {

    private val noteId: String = checkNotNull(savedStateHandle[Routes.NOTE_ID])

    val uiState: StateFlow<NoteDetailUiState> = repository
        .getNoteById(noteId)
        .map { note ->
            NoteDetailUiState(
                isLoading = false,
                note = note
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
            repository.deleteNote(note)
            onDeleted()
        }
    }
}