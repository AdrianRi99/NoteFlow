package com.production.noteflow.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.production.noteflow.data.local.entities.NoteEntity
import com.production.noteflow.data.local.repository.NoteRepository
import com.production.noteflow.data.remote.repository.QuoteRepository
import com.production.noteflow.domain.Quote
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

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.production.noteflow.data.local.entities.NoteEntity
//import com.production.noteflow.data.local.repository.NoteRepository
//import com.production.noteflow.data.remote.repository.QuoteRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class DashboardViewModel @Inject constructor(
//    repository: NoteRepository,
//    private val quoteRepository: QuoteRepository
//) : ViewModel() {
//
//    val notes: StateFlow<List<NoteEntity>> = repository
//        .getNotes()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = emptyList()
//        )
//
//    private val _quoteUiState = MutableStateFlow(QuoteUiState(isLoading = true))
//    val quoteUiState: StateFlow<QuoteUiState> = _quoteUiState.asStateFlow()
//
//    init {
//        refreshQuote()
//    }
//
//    fun refreshQuote() {
//        viewModelScope.launch {
//            _quoteUiState.value = QuoteUiState(isLoading = true)
//
//            runCatching {
//                quoteRepository.getRandomQuote()
//            }.onSuccess { quote ->
//                _quoteUiState.value = QuoteUiState(
//                    isLoading = false,
//                    quote = quote,
//                    errorMessage = null
//                )
//            }.onFailure {
//                _quoteUiState.value = QuoteUiState(
//                    isLoading = false,
//                    quote = null,
//                    errorMessage = "Zitat konnte nicht geladen werden."
//                )
//            }
//        }
//    }
//}