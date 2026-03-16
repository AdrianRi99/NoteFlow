package com.production.noteflow.domain.usecase.quote

import com.production.noteflow.domain.repository.QuoteRepository
import javax.inject.Inject

class GetStoredQuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    operator fun invoke() = quoteRepository.storedQuote
}