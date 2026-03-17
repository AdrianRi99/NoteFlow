package com.production.noteflow.fake

import com.production.noteflow.domain.model.StoredQuote
import com.production.noteflow.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeQuoteRepository : QuoteRepository {

    private val quoteFlow = MutableStateFlow<StoredQuote?>(null)

    var refreshShouldFail: Boolean = false
    var lastForceValue: Boolean? = null

    override val storedQuote: Flow<StoredQuote?> = quoteFlow

    override suspend fun refreshQuote(force: Boolean): Result<Unit> {
        lastForceValue = force
        return if (refreshShouldFail) {
            Result.failure(IllegalStateException("Refresh failed"))
        } else {
            if (quoteFlow.value == null || force) {
                quoteFlow.value = StoredQuote(
                    text = "Test quote",
                    author = "Tester",
                    lastUpdatedMillis = System.currentTimeMillis()
                )
            }
            Result.success(Unit)
        }
    }

    fun seed(quote: StoredQuote?) {
        quoteFlow.value = quote
    }
}