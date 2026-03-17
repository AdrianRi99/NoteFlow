package com.production.noteflow.data.local.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.production.noteflow.data.local.room.daos.NoteDao
import com.production.noteflow.data.local.room.entities.NoteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = db.noteDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_get_note_by_id() = runTest {
        val note = NoteEntity(
            id = "1",
            title = "Title",
            subtitle = "Subtitle",
            tag = "Ideas",
            content = "Content",
            createdAt = 100L,
            imageUri = null
        )

        noteDao.insertNote(note)

        val result = noteDao.getNoteByIdOnce("1")

        assertEquals("Title", result?.title)
    }

    @Test
    fun getNotes_returns_sorted_by_createdAt_desc() = runTest {
        val oldNote = NoteEntity("1", "Old", "", "Ideas", "", 1L, null)
        val newNote = NoteEntity("2", "New", "", "Ideas", "", 2L, null)

        noteDao.insertNote(oldNote)
        noteDao.insertNote(newNote)

        val notes = noteDao.getNotes().first()

        assertEquals("2", notes[0].id)
        assertEquals("1", notes[1].id)
    }

    @Test
    fun delete_removes_note() = runTest {
        val note = NoteEntity("1", "Title", "", "Ideas", "", 1L, null)
        noteDao.insertNote(note)

        noteDao.deleteNote(note)

        val result = noteDao.getNoteByIdOnce("1")
        assertNull(result)
    }
}