package com.production.noteflow.data.repository

import com.production.noteflow.data.local.NoteDao
import com.production.noteflow.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getNotes(): Flow<List<NoteEntity>> = noteDao.getNotes()

    suspend fun getNoteById(id: String): NoteEntity? = noteDao.getNoteById(id)

    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)
}