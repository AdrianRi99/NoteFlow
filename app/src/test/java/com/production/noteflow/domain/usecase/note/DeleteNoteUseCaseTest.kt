package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.fake.FakeNoteRepository
import com.production.noteflow.fake.FakeReminderRepository
import com.production.noteflow.fake.FakeReminderScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class DeleteNoteUseCaseTest {

    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var reminderScheduler: FakeReminderScheduler
    private lateinit var useCase: DeleteNoteUseCase

    private val note = Note(
        id = "n1",
        title = "Title",
        subtitle = "",
        tag = "Ideas",
        content = "Content",
        createdAt = 1L,
        imageUri = null
    )

    @Before
    fun setup() {
        noteRepository = FakeNoteRepository()
        reminderRepository = FakeReminderRepository()
        reminderScheduler = FakeReminderScheduler()
        useCase = DeleteNoteUseCase(noteRepository, reminderRepository, reminderScheduler)

        noteRepository.seed(listOf(note))
        reminderRepository.seed(
            listOf(
                Reminder("r1", "n1", 1, 9, 0, true),
                Reminder("r2", "n1", 2, 10, 0, true)
            )
        )
    }

    @Test
    fun `deletes note and cancels reminders`() = runTest {
        useCase(note)

        assertNull(noteRepository.getNoteByIdOnce("n1"))
        assertEquals(2, reminderScheduler.cancelled.size)
    }
}