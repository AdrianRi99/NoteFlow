package com.production.noteflow.data.local.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.production.noteflow.data.local.room.daos.NoteDao
import com.production.noteflow.data.local.room.daos.ReminderDao
import com.production.noteflow.data.local.room.entities.NoteEntity
import com.production.noteflow.data.local.room.entities.ReminderEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReminderDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var reminderDao: ReminderDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = db.noteDao()
        reminderDao = db.reminderDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getRemindersForNote_returns_sorted_by_day_hour_minute() = runTest {
        noteDao.insertNote(
            NoteEntity("n1", "Title", "", "Ideas", "", 1L, null)
        )

        reminderDao.insertReminders(
            listOf(
                ReminderEntity("r3", "n1", 3, 10, 0, true),
                ReminderEntity("r1", "n1", 1, 9, 0, true),
                ReminderEntity("r2", "n1", 1, 8, 30, true)
            )
        )

        val reminders = reminderDao.getRemindersForNote("n1").first()

        assertEquals("r2", reminders[0].id)
        assertEquals("r1", reminders[1].id)
        assertEquals("r3", reminders[2].id)
    }

    @Test
    fun deleteRemindersForNote_removes_only_matching_note() = runTest {
        noteDao.insertNote(NoteEntity("n1", "A", "", "Ideas", "", 1L, null))
        noteDao.insertNote(NoteEntity("n2", "B", "", "Ideas", "", 1L, null))

        reminderDao.insertReminders(
            listOf(
                ReminderEntity("r1", "n1", 1, 9, 0, true),
                ReminderEntity("r2", "n2", 2, 10, 0, true)
            )
        )

        reminderDao.deleteRemindersForNote("n1")

        val n1 = reminderDao.getRemindersForNoteOnce("n1")
        val n2 = reminderDao.getRemindersForNoteOnce("n2")

        assertEquals(0, n1.size)
        assertEquals(1, n2.size)
    }
}