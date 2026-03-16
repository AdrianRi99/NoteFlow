package com.production.noteflow.domain.repository

import com.production.noteflow.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    fun getNoteById(id: String): Flow<Note?>
    suspend fun getNoteByIdOnce(id: String): Note?
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}