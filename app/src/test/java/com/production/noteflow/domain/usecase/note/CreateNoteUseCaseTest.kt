package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.ReminderDraft
import com.production.noteflow.fake.FakeNoteRepository
import com.production.noteflow.fake.FakeReminderRepository
import com.production.noteflow.fake.FakeReminderScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateNoteUseCaseTest {

    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var reminderScheduler: FakeReminderScheduler
    private lateinit var useCase: CreateNoteUseCase

    @Before
    fun setup() {
        noteRepository = FakeNoteRepository()
        reminderRepository = FakeReminderRepository()
        reminderScheduler = FakeReminderScheduler()
        useCase = CreateNoteUseCase(
            noteRepository = noteRepository,
            reminderRepository = reminderRepository,
            reminderScheduler = reminderScheduler
        )
    }

    @Test
    fun `creates note and schedules enabled reminders`() = runTest {
        val reminders = listOf(
            ReminderDraft(1, true, 9, 0),
            ReminderDraft(2, false, 10, 30)
        )

        val result = useCase(
            title = "My title",
            subtitle = "My subtitle",
            content = "My content",
            tag = "Ideas",
            imageUri = null,
            reminders = reminders
        )

        assertTrue(result.isSuccess)

        val createdNotes = noteRepository.getNotes().first()

        assertEquals(1, createdNotes.size)
        val noteId = createdNotes.first().id

        val storedReminders = reminderRepository.getRemindersForNoteOnce(noteId)
        assertEquals(1, storedReminders.size)
        assertEquals(1, reminderScheduler.scheduled.size)
    }

    @Test
    fun `fails when title is blank`() = runTest {
        val result = useCase(
            title = "   ",
            subtitle = "",
            content = "",
            tag = "Ideas",
            imageUri = null,
            reminders = emptyList()
        )

        assertTrue(result.isFailure)
    }
}