package com.production.noteflow.ui.screen.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.NoteEntity
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NoteRepository
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

    var selectedTag by mutableStateOf("Work")
        private set

    private var originalNote: NoteEntity? = null

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            val note = repository.getNoteById(noteId).firstOrNull()
            originalNote = note

            if (note != null) {
                title = note.title
                subtitle = note.subtitle
                content = note.content
                selectedTag = note.tag
            }

            isLoading = false
        }
    }

    fun onTitleChange(value: String) { title = value }
    fun onSubtitleChange(value: String) { subtitle = value }
    fun onContentChange(value: String) { content = value }
    fun onTagChange(value: String) { selectedTag = value }

    fun updateNote(onSaved: () -> Unit) {
        val note = originalNote ?: return
        if (title.isBlank()) return

        viewModelScope.launch {
            repository.updateNote(
                note.copy(
                    title = title.trim(),
                    subtitle = subtitle.trim(),
                    content = content.trim(),
                    tag = selectedTag
                )
            )
            onSaved()
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        val note = originalNote ?: return

        viewModelScope.launch {
            repository.deleteNote(note)
            onDeleted()
        }
    }
}