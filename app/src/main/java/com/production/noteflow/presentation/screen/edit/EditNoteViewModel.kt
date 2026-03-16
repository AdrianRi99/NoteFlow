package com.production.noteflow.presentation.screen.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.app.navigation.Routes
import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.ReminderDraftFactory
import com.production.noteflow.domain.usecase.note.DeleteNoteUseCase
import com.production.noteflow.domain.usecase.note.GetNoteByIdUseCase
import com.production.noteflow.domain.usecase.note.UpdateNoteUseCase
import com.production.noteflow.domain.usecase.reminder.GetRemindersForNoteOnceUseCase
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
) : ViewModel() {

    private val noteId: String = checkNotNull(savedStateHandle[Routes.NOTE_ID])

    var isLoading by mutableStateOf(true)
        private set

    var title by mutableStateOf("")
        private set

    var subtitle by mutableStateOf("")
        private set

    var content by mutableStateOf("")
        private set

    var selectedTag by mutableStateOf("Ideas")
        private set

    var selectedImageUri by mutableStateOf<String?>(null)
        private set

    var reminders by mutableStateOf(ReminderDraftFactory.defaultReminderDrafts())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var originalNote: Note? = null

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            val note = getNoteByIdUseCase(noteId).first()
            val reminderModels = getRemindersForNoteOnceUseCase(noteId)

            originalNote = note

            if (note != null) {
                title = note.title
                subtitle = note.subtitle
                content = note.content
                selectedTag = note.tag
                selectedImageUri = note.imageUri
            }

            reminders = ReminderDraftFactory.defaultReminderDrafts().map { draft ->
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

            isLoading = false
        }
    }

    fun onTitleChange(value: String) { title = value }
    fun onSubtitleChange(value: String) { subtitle = value }
    fun onContentChange(value: String) { content = value }
    fun onTagChange(value: String) { selectedTag = value }
    fun onImageSelected(uri: String?) { selectedImageUri = uri }
    fun removeImage() { selectedImageUri = null }

    fun toggleReminderDay(dayOfWeek: Int) {
        reminders = reminders.map {
            if (it.dayOfWeek == dayOfWeek) it.copy(enabled = !it.enabled) else it
        }
    }

    fun updateReminderTime(dayOfWeek: Int, hour: Int, minute: Int) {
        reminders = reminders.map {
            if (it.dayOfWeek == dayOfWeek) {
                it.copy(hour = hour, minute = minute)
            } else {
                it
            }
        }
    }

    fun updateNote(onSaved: () -> Unit) {
        val note = originalNote ?: return

        viewModelScope.launch {
            updateNoteUseCase(
                originalNote = note,
                title = title,
                subtitle = subtitle,
                content = content,
                tag = selectedTag,
                imageUri = selectedImageUri,
                reminders = reminders
            ).onSuccess {
                onSaved()
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        val note = originalNote ?: return

        viewModelScope.launch {
            deleteNoteUseCase(note)
                .onSuccess { onDeleted() }
                .onFailure { errorMessage = it.message }
        }
    }
}