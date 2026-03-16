package com.production.noteflow.presentation.screen.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.production.noteflow.app.navigation.Routes
import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.ReminderDraftFactory
import com.production.noteflow.domain.usecase.note.DeleteNoteUseCase
import com.production.noteflow.domain.usecase.note.GetNoteByIdUseCase
import com.production.noteflow.domain.usecase.note.UpdateNoteUseCase
import com.production.noteflow.domain.usecase.reminder.GetRemindersForNoteOnceUseCase
import com.production.noteflow.presentation.common.BaseNoteEditorViewModel
import com.production.noteflow.presentation.model.NoteEditorEvent
import com.production.noteflow.presentation.model.NoteEditorFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val getRemindersForNoteOnceUseCase: GetRemindersForNoteOnceUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : BaseNoteEditorViewModel() {

    private val noteId: String = checkNotNull(savedStateHandle[Routes.NOTE_ID])

    private var originalNote: Note? = null

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            setLoading(true)

            val note = getNoteByIdUseCase(noteId).first()
            val reminderModels = getRemindersForNoteOnceUseCase(noteId)

            originalNote = note

            val mergedReminders = ReminderDraftFactory.defaultReminderDrafts().map { draft ->
                val existing = reminderModels.firstOrNull { it.dayOfWeek == draft.dayOfWeek }
                if (existing != null) {
                    draft.copy(
                        enabled = true,
                        hour = existing.hour,
                        minute = existing.minute
                    )
                } else {
                    draft
                }
            }

            if (note != null) {
                updateFormState(
                    NoteEditorFormState(
                        title = note.title,
                        subtitle = note.subtitle,
                        content = note.content,
                        selectedTag = note.tag,
                        selectedImageUri = note.imageUri,
                        reminders = mergedReminders,
                        isLoading = false,
                        errorMessage = null
                    )
                )
            } else {
                updateFormState(
                    NoteEditorFormState(
                        reminders = mergedReminders,
                        isLoading = false
                    )
                )
            }
        }
    }

    fun updateNote() {
        val note = originalNote ?: return
        val state = uiState.value

        viewModelScope.launch {
            updateNoteUseCase(
                originalNote = note,
                title = state.title,
                subtitle = state.subtitle,
                content = state.content,
                tag = state.selectedTag,
                imageUri = state.selectedImageUri,
                reminders = state.reminders
            ).onSuccess {
                sendEvent(NoteEditorEvent.Saved)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Aktualisieren fehlgeschlagen."
                setError(message)
                sendEvent(NoteEditorEvent.ShowError(message))
            }
        }
    }

    fun deleteNote() {
        val note = originalNote ?: return

        viewModelScope.launch {
            deleteNoteUseCase(note)
                .onSuccess {
                    sendEvent(NoteEditorEvent.Deleted)
                }
                .onFailure { throwable ->
                    val message = throwable.message ?: "Löschen fehlgeschlagen."
                    setError(message)
                    sendEvent(NoteEditorEvent.ShowError(message))
                }
        }
    }
}