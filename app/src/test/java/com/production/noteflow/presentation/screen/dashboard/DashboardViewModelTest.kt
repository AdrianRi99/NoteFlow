package com.production.noteflow.presentation.screen.dashboard

import com.production.noteflow.util.MainDispatcherRule
import com.production.noteflow.domain.usecase.note.GetNotesUseCase
import com.production.noteflow.domain.usecase.quote.GetStoredQuoteUseCase
import com.production.noteflow.domain.usecase.quote.RefreshQuoteUseCase
import com.production.noteflow.fake.FakeNoteRepository
import com.production.noteflow.fake.FakeQuoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `refreshQuote forces refresh`() = runTest {
        val noteRepository = FakeNoteRepository()
        val quoteRepository = FakeQuoteRepository()

        val viewModel = DashboardViewModel(
            getNotesUseCase = GetNotesUseCase(noteRepository),
            getStoredQuoteUseCase = GetStoredQuoteUseCase(quoteRepository),
            refreshQuoteUseCase = RefreshQuoteUseCase(quoteRepository)
        )

        viewModel.refreshQuote()
        advanceUntilIdle()

        assertTrue(quoteRepository.lastForceValue == true)
    }
}