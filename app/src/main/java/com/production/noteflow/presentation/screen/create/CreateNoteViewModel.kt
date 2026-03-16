package com.production.noteflow.presentation.screen.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.domain.model.ReminderDraftFactory
import com.production.noteflow.domain.usecase.note.CreateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase
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


    var reminders by mutableStateOf(ReminderDraftFactory.defaultReminderDrafts())
        private set

    var errorMessage by mutableStateOf<String?>(null)
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
            } else {
                it
            }
        }
    }

    fun saveNote(onSaved: () -> Unit) {
        viewModelScope.launch {
            createNoteUseCase(
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
}