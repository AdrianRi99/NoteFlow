package com.production.noteflow.presentation.screen.dashboard

import com.production.noteflow.domain.model.Quote

data class QuoteUiState(
    val isLoading: Boolean = false,
    val quote: Quote? = null,
    val errorMessage: String? = null
)