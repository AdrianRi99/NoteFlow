package com.production.noteflow.presentation.screen.edit

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.production.noteflow.app.navigation.Routes
import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.usecase.note.DeleteNoteUseCase
import com.production.noteflow.domain.usecase.note.GetNoteByIdUseCase
import com.production.noteflow.domain.usecase.note.UpdateNoteUseCase
import com.production.noteflow.domain.usecase.reminder.GetRemindersForNoteOnceUseCase
import com.production.noteflow.fake.FakeNoteRepository
import com.production.noteflow.fake.FakeReminderRepository
import com.production.noteflow.fake.FakeReminderScheduler
import com.production.noteflow.presentation.model.NoteEditorEvent
import com.production.noteflow.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditNoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var reminderScheduler: FakeReminderScheduler

    @Before
    fun setup() {
        noteRepository = FakeNoteRepository()
        reminderRepository = FakeReminderRepository()
        reminderScheduler = FakeReminderScheduler()

        noteRepository.seed(
            listOf(
                Note(
                    id = "note-1",
                    title = "Old title",
                    subtitle = "Old subtitle",
                    tag = "Ideas",
                    content = "Old content",
                    createdAt = 1L,
                    imageUri = null
                )
            )
        )
    }

    @Test
    fun `loads note into uiState`() = runTest {
        val viewModel = EditNoteViewModel(
            savedStateHandle = SavedStateHandle(mapOf(Routes.NOTE_ID to "note-1")),
            getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository),
            getRemindersForNoteOnceUseCase = GetRemindersForNoteOnceUseCase(reminderRepository),
            updateNoteUseCase = UpdateNoteUseCase(noteRepository, reminderRepository, reminderScheduler),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepository, reminderRepository, reminderScheduler)
        )

        advanceUntilIdle()

        assertEquals("Old title", viewModel.uiState.value.title)
        assertEquals("Old subtitle", viewModel.uiState.value.subtitle)
    }

    @Test
    fun `updateNote emits Saved event`() = runTest {
        val viewModel = EditNoteViewModel(
            savedStateHandle = SavedStateHandle(mapOf(Routes.NOTE_ID to "note-1")),
            getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository),
            getRemindersForNoteOnceUseCase = GetRemindersForNoteOnceUseCase(reminderRepository),
            updateNoteUseCase = UpdateNoteUseCase(noteRepository, reminderRepository, reminderScheduler),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepository, reminderRepository, reminderScheduler)
        )

        advanceUntilIdle()
        viewModel.onTitleChange("Updated title")

        viewModel.events.test {
            viewModel.updateNote()
            advanceUntilIdle()

            assertEquals(NoteEditorEvent.Saved, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}