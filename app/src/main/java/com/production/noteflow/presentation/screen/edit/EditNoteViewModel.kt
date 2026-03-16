package com.production.noteflow.presentation.screen.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.room.entities.NoteEntity
import com.production.noteflow.data.local.room.entities.ReminderEntity
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.data.repository.ReminderRepository
import com.production.noteflow.presentation.model.ReminderDraft
import com.production.noteflow.services.reminder.ReminderScheduler
import com.production.noteflow.app.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler
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

    var reminders by mutableStateOf(defaultReminderDrafts())
        private set

    private var originalNote: NoteEntity? = null

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId).first()
            val reminderEntities = reminderRepository.getRemindersForNoteOnce(noteId)

            originalNote = note

            if (note != null) {
                title = note.title
                subtitle = note.subtitle
                content = note.content
                selectedTag = note.tag
                selectedImageUri = note.imageUri
            }

            reminders = defaultReminderDrafts().map { draft ->
                val existing = reminderEntities.firstOrNull { it.dayOfWeek == draft.dayOfWeek }
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
            } else it
        }
    }

    fun updateNote(onSaved: () -> Unit) {
        val note = originalNote ?: return
        if (title.isBlank()) return

        viewModelScope.launch {
            noteRepository.updateNote(
                note.copy(
                    title = title.trim(),
                    subtitle = subtitle.trim(),
                    content = content.trim(),
                    tag = selectedTag,
                    imageUri = selectedImageUri
                )
            )

            val oldReminders = reminderRepository.getRemindersForNoteOnce(noteId)
            oldReminders.forEach(reminderScheduler::cancel)

            val newReminders = reminders
                .filter { it.enabled }
                .map {
                    ReminderEntity(
                        id = UUID.randomUUID().toString(),
                        noteId = noteId,
                        dayOfWeek = it.dayOfWeek,
                        hour = it.hour,
                        minute = it.minute,
                        enabled = true
                    )
                }

            reminderRepository.replaceRemindersForNote(noteId, newReminders)
            newReminders.forEach(reminderScheduler::schedule)

            onSaved()
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        val note = originalNote ?: return

        viewModelScope.launch {
            val oldReminders = reminderRepository.getRemindersForNoteOnce(noteId)
            oldReminders.forEach(reminderScheduler::cancel)
            noteRepository.deleteNote(note)
            onDeleted()
        }
    }

    private companion object {
        fun defaultReminderDrafts() = listOf(
            ReminderDraft(1, false, 9, 0),
            ReminderDraft(2, false, 9, 0),
            ReminderDraft(3, false, 9, 0),
            ReminderDraft(4, false, 9, 0),
            ReminderDraft(5, false, 9, 0),
            ReminderDraft(6, false, 9, 0),
            ReminderDraft(7, false, 9, 0)
        )
    }
}