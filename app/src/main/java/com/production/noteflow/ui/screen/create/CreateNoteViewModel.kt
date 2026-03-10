package com.production.noteflow.ui.screen.create


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.entities.NoteEntity
import com.production.noteflow.data.local.entities.ReminderEntity
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.data.repository.ReminderRepository
import com.production.noteflow.domain.ReminderDraft
import com.production.noteflow.services.reminder.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

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

    fun saveNote(onSaved: () -> Unit) {
        if (title.isBlank()) return

        viewModelScope.launch {
            val noteId = UUID.randomUUID().toString()

            noteRepository.insertNote(
                NoteEntity(
                    id = noteId,
                    title = title.trim(),
                    subtitle = subtitle.trim(),
                    tag = selectedTag,
                    content = content.trim(),
                    createdAt = System.currentTimeMillis(),
                    imageUri = selectedImageUri
                )
            )

            val reminderEntities = reminders
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

            if (reminderEntities.isNotEmpty()) {
                reminderRepository.replaceRemindersForNote(noteId, reminderEntities)
                reminderEntities.forEach(reminderScheduler::schedule)
            }

            onSaved()
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