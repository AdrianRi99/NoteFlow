package com.production.noteflow.data.repository

import com.production.noteflow.data.local.room.daos.NoteDao
import com.production.noteflow.data.mapper.toDomain
import com.production.noteflow.data.mapper.toEntity
import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes().map { list -> list.map { it.toDomain() } }
    }

    override fun getNoteById(id: String): Flow<Note?> {
        return noteDao.getNoteById(id).map { it?.toDomain() }
    }

    override suspend fun getNoteByIdOnce(id: String): Note? {
        return noteDao.getNoteByIdOnce(id)?.toDomain()
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }
}