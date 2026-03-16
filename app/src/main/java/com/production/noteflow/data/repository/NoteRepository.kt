package com.production.noteflow.data.repository

import com.production.noteflow.data.local.room.daos.NoteDao
import com.production.noteflow.data.local.room.entities.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getNotes(): Flow<List<NoteEntity>> = noteDao.getNotes()

    fun getNoteById(id: String): Flow<NoteEntity?> = noteDao.getNoteById(id)

    suspend fun getNoteByIdOnce(id: String): NoteEntity? = noteDao.getNoteByIdOnce(id)

    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun deleteNote(note: NoteEntity) = noteDao.deleteNote(note)
}