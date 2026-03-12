package com.production.noteflow.ui.screen.dashboard

import com.production.noteflow.domain.Quote

data class QuoteUiState(
    val isLoading: Boolean = false,
    val quote: Quote? = null,
    val errorMessage: String? = null
)