package com.production.noteflow.fake

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeNoteRepository : NoteRepository {

    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())

    override fun getNotes(): Flow<List<Note>> = notesFlow

    override fun getNoteById(id: String): Flow<Note?> {
        return notesFlow.map { notes -> notes.firstOrNull { it.id == id } }
    }

    override suspend fun getNoteByIdOnce(id: String): Note? {
        return notesFlow.value.firstOrNull { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        notesFlow.value = listOf(note) + notesFlow.value
    }

    override suspend fun updateNote(note: Note) {
        notesFlow.value = notesFlow.value.map {
            if (it.id == note.id) note else it
        }
    }

    override suspend fun deleteNote(note: Note) {
        notesFlow.value = notesFlow.value.filterNot { it.id == note.id }
    }

    fun seed(notes: List<Note>) {
        notesFlow.value = notes
    }
}