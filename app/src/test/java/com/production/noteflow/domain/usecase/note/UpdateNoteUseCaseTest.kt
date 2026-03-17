package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.model.ReminderDraft
import com.production.noteflow.fake.FakeNoteRepository
import com.production.noteflow.fake.FakeReminderRepository
import com.production.noteflow.fake.FakeReminderScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateNoteUseCaseTest {

    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var reminderScheduler: FakeReminderScheduler
    private lateinit var useCase: UpdateNoteUseCase

    private val existingNote = Note(
        id = "note-1",
        title = "Old",
        subtitle = "Old sub",
        tag = "Ideas",
        content = "Old content",
        createdAt = 1L,
        imageUri = null
    )

    @Before
    fun setup() {
        noteRepository = FakeNoteRepository()
        reminderRepository = FakeReminderRepository()
        reminderScheduler = FakeReminderScheduler()
        useCase = UpdateNoteUseCase(noteRepository, reminderRepository, reminderScheduler)

        noteRepository.seed(listOf(existingNote))
        reminderRepository.seed(
            listOf(
                Reminder("r1", "note-1", 1, 9, 0, true),
                Reminder("r2", "note-1", 3, 11, 15, true)
            )
        )
    }

    @Test
    fun `updates note and replaces reminders`() = runTest {
        val result = useCase(
            originalNote = existingNote,
            title = "New",
            subtitle = "New sub",
            content = "New content",
            tag = "Projects",
            imageUri = "image://1",
            reminders = listOf(
                ReminderDraft(5, true, 14, 30)
            )
        )

        assertTrue(result.isSuccess)

        val updated = noteRepository.getNoteByIdOnce("note-1")!!
        assertEquals("New", updated.title)
        assertEquals("Projects", updated.tag)
        assertEquals("image://1", updated.imageUri)

        val reminders = reminderRepository.getRemindersForNoteOnce("note-1")
        assertEquals(1, reminders.size)
        assertEquals(5, reminders.first().dayOfWeek)

        assertEquals(2, reminderScheduler.cancelled.size)
        assertEquals(1, reminderScheduler.scheduled.size)
    }
}