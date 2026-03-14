package com.production.noteflow.data.remote.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.production.noteflow.data.local.datastore.QuotePreferencesDataSource
import com.production.noteflow.data.remote.QuoteApiService
import com.production.noteflow.data.remote.dto.toDomain
import com.production.noteflow.domain.StoredQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class QuoteRepository @Inject constructor(
    private val quoteApiService: QuoteApiService,
    private val quotePreferencesDataSource: QuotePreferencesDataSource
) {

    val storedQuote: Flow<StoredQuote?> = quotePreferencesDataSource.storedQuote

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshQuote(force: Boolean): Result<Unit> {
        return runCatching {
            val shouldRefresh = force || !wasUpdatedToday()
            if (!shouldRefresh) return@runCatching

            val quote = quoteApiService.getRandomQuote().toDomain()
            quotePreferencesDataSource.saveQuote(
                quote = quote,
                updatedAtMillis = System.currentTimeMillis()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun wasUpdatedToday(): Boolean {
        val current = storedQuote.first() ?: return false
        val zoneId = ZoneId.systemDefault()

        val lastDate = Instant.ofEpochMilli(current.lastUpdatedMillis)
            .atZone(zoneId)
            .toLocalDate()

        val today = LocalDate.now(zoneId)
        return lastDate == today
    }
}
