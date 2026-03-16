package com.production.noteflow.domain.usecase.quote

import com.production.noteflow.domain.repository.QuoteRepository
import javax.inject.Inject

class RefreshQuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(force: Boolean): Result<Unit> {
        return quoteRepository.refreshQuote(force)
    }
}