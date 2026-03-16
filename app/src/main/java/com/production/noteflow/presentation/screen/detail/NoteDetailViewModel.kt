package com.production.noteflow.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.data.repository.ReminderRepository
import com.production.noteflow.services.reminder.ReminderScheduler
import com.production.noteflow.app.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val noteId: String = checkNotNull(savedStateHandle[Routes.NOTE_ID])

    val uiState: StateFlow<NoteDetailUiState> = combine(
        noteRepository.getNoteById(noteId),
        reminderRepository.getRemindersForNote(noteId)
    ) { note, reminders ->
        NoteDetailUiState(
            isLoading = false,
            note = note,
            reminders = reminders
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NoteDetailUiState()
    )

    fun deleteCurrentNote(onDeleted: () -> Unit) {
        val note = uiState.value.note ?: return
        viewModelScope.launch {
            val reminders = reminderRepository.getRemindersForNoteOnce(note.id)
            reminders.forEach(reminderScheduler::cancel)
            noteRepository.deleteNote(note)
            onDeleted()
        }
    }
}