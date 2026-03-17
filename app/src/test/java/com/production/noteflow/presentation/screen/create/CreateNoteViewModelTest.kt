package com.production.noteflow.presentation.screen.create

import app.cash.turbine.test
import com.production.noteflow.domain.usecase.note.CreateNoteUseCase
import com.production.noteflow.fake.FakeNoteRepository
import com.production.noteflow.fake.FakeReminderRepository
import com.production.noteflow.fake.FakeReminderScheduler
import com.production.noteflow.presentation.model.NoteEditorEvent
import com.production.noteflow.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateNoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `saveNote emits Saved event on success`() = runTest {
        val viewModel = CreateNoteViewModel(
            createNoteUseCase = CreateNoteUseCase(
                noteRepository = FakeNoteRepository(),
                reminderRepository = FakeReminderRepository(),
                reminderScheduler = FakeReminderScheduler()
            )
        )

        viewModel.onTitleChange("My note")

        viewModel.events.test {
            viewModel.saveNote()
            advanceUntilIdle()

            assertEquals(NoteEditorEvent.Saved, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveNote emits ShowError when title is blank`() = runTest {
        val viewModel = CreateNoteViewModel(
            createNoteUseCase = CreateNoteUseCase(
                noteRepository = FakeNoteRepository(),
                reminderRepository = FakeReminderRepository(),
                reminderScheduler = FakeReminderScheduler()
            )
        )

        viewModel.events.test {
            viewModel.saveNote()
            advanceUntilIdle()

            val event = awaitItem()
            assert(event is NoteEditorEvent.ShowError)
            cancelAndIgnoreRemainingEvents()
        }
    }
}