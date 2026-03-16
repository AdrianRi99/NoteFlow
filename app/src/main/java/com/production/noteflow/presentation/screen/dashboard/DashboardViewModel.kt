package com.production.noteflow.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.room.entities.NoteEntity
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.data.repository.QuoteRepository
import com.production.noteflow.domain.model.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: NoteRepository,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    val notes: StateFlow<List<NoteEntity>> = repository
        .getNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val isRefreshing = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val quoteUiState: StateFlow<QuoteUiState> = combine(
        quoteRepository.storedQuote,
        isRefreshing,
        errorMessage
    ) { storedQuote, refreshing, error ->
        QuoteUiState(
            isLoading = refreshing,
            quote = storedQuote?.let { Quote(it.text, it.author) },
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = QuoteUiState(isLoading = false)
    )

    init {
        loadQuoteIfNeeded()
    }

    private fun loadQuoteIfNeeded() {
        viewModelScope.launch {
            isRefreshing.value = true
            errorMessage.value = null

            quoteRepository.refreshQuote(force = false)
                .onFailure {
                    if (quoteUiState.value.quote == null) {
                        errorMessage.value = "Zitat konnte nicht geladen werden."
                    }
                }

            isRefreshing.value = false
        }
    }

    fun refreshQuote() {
        viewModelScope.launch {
            isRefreshing.value = true
            errorMessage.value = null

            quoteRepository.refreshQuote(force = true)
                .onFailure {
                    errorMessage.value = "Zitat konnte nicht geladen werden."
                }

            isRefreshing.value = false
        }
    }
}
