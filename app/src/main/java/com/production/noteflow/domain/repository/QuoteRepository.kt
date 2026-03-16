package com.production.noteflow.domain.repository

import com.production.noteflow.domain.model.StoredQuote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    val storedQuote: Flow<StoredQuote?>
    suspend fun refreshQuote(force: Boolean): Result<Unit>
}