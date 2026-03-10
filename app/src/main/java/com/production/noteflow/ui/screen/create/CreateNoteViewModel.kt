package com.production.noteflow.ui.screen.create


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.NoteEntity
import com.production.noteflow.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var subtitle by mutableStateOf("")
        private set

    var content by mutableStateOf("")
        private set

    var selectedTag by mutableStateOf("Work")
        private set

    var selectedImageUri by mutableStateOf<String?>(null)
        private set

    fun onTitleChange(value: String) { title = value }
    fun onSubtitleChange(value: String) { subtitle = value }
    fun onContentChange(value: String) { content = value }
    fun onTagChange(value: String) { selectedTag = value }

    fun onImageSelected(uri: String?) {
        selectedImageUri = uri
    }

    fun removeImage() {
        selectedImageUri = null
    }

    fun saveNote(onSaved: () -> Unit) {
        if (title.isBlank()) return

        viewModelScope.launch {
            repository.insertNote(
                NoteEntity(
                    id = UUID.randomUUID().toString(),
                    title = title.trim(),
                    subtitle = subtitle.trim(),
                    tag = selectedTag,
                    content = content.trim(),
                    createdAt = System.currentTimeMillis(),
                    imageUri = selectedImageUri
                )
            )
            onSaved()
        }
    }
}