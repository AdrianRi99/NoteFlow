package com.production.noteflow.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.entities.NoteEntity
import com.production.noteflow.data.local.repository.NoteRepository
import com.production.noteflow.data.remote.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _quoteUiState = MutableStateFlow(QuoteUiState(isLoading = true))
    val quoteUiState: StateFlow<QuoteUiState> = _quoteUiState.asStateFlow()

    init {
        refreshQuote()
    }

    fun refreshQuote() {
        viewModelScope.launch {
            _quoteUiState.value = QuoteUiState(isLoading = true)

            runCatching {
                quoteRepository.getRandomQuote()
            }.onSuccess { quote ->
                _quoteUiState.value = QuoteUiState(
                    isLoading = false,
                    quote = quote,
                    errorMessage = null
                )
            }.onFailure {
                _quoteUiState.value = QuoteUiState(
                    isLoading = false,
                    quote = null,
                    errorMessage = "Zitat konnte nicht geladen werden."
                )
            }
        }
    }
}