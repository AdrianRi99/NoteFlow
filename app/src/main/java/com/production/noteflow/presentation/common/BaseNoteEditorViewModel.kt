package com.production.noteflow.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.presentation.model.NoteEditorEvent
import com.production.noteflow.presentation.model.NoteEditorFormState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseNoteEditorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorFormState())
    val uiState: StateFlow<NoteEditorFormState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NoteEditorEvent>()
    val events: SharedFlow<NoteEditorEvent> = _events.asSharedFlow()

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onSubtitleChange(value: String) {
        _uiState.update { it.copy(subtitle = value) }
    }

    fun onContentChange(value: String) {
        _uiState.update { it.copy(content = value) }
    }

    fun onTagChange(value: String) {
        _uiState.update { it.copy(selectedTag = value) }
    }

    fun onImageSelected(uri: String?) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun removeImage() {
        _uiState.update { it.copy(selectedImageUri = null) }
    }

    fun toggleReminderDay(dayOfWeek: Int) {
        _uiState.update { state ->
            state.copy(
                reminders = state.reminders.map { reminder ->
                    if (reminder.dayOfWeek == dayOfWeek) {
                        reminder.copy(enabled = !reminder.enabled)
                    } else {
                        reminder
                    }
                }
            )
        }
    }

    fun updateReminderTime(dayOfWeek: Int, hour: Int, minute: Int) {
        _uiState.update { state ->
            state.copy(
                reminders = state.reminders.map { reminder ->
                    if (reminder.dayOfWeek == dayOfWeek) {
                        reminder.copy(hour = hour, minute = minute)
                    } else {
                        reminder
                    }
                }
            )
        }
    }

    protected fun setLoading(value: Boolean) {
        _uiState.update { it.copy(isLoading = value) }
    }

    protected fun setError(message: String?) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    protected fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    protected fun updateFormState(newState: NoteEditorFormState) {
        _uiState.value = newState
    }

    protected fun sendEvent(event: NoteEditorEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}