package com.production.noteflow.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.production.noteflow.domain.Quote
import com.production.noteflow.domain.StoredQuote
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.quoteDataStore by preferencesDataStore(name = "quote_preferences")

@Singleton
class QuotePreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val quoteText = stringPreferencesKey("quote_text")
        val quoteAuthor = stringPreferencesKey("quote_author")
        val lastUpdatedMillis = longPreferencesKey("quote_last_updated_millis")
    }

    val storedQuote: Flow<StoredQuote?> = context.quoteDataStore.data.map { prefs ->
        val text = prefs[Keys.quoteText]
        val author = prefs[Keys.quoteAuthor]
        val lastUpdated = prefs[Keys.lastUpdatedMillis]

        if (text != null && author != null && lastUpdated != null) {
            StoredQuote(
                text = text,
                author = author,
                lastUpdatedMillis = lastUpdated
            )
        } else {
            null
        }
    }

    suspend fun saveQuote(quote: Quote, updatedAtMillis: Long) {
        context.quoteDataStore.edit { prefs ->
            prefs[Keys.quoteText] = quote.text
            prefs[Keys.quoteAuthor] = quote.author
            prefs[Keys.lastUpdatedMillis] = updatedAtMillis
        }
    }

    suspend fun clear() {
        context.quoteDataStore.edit { it.clear() }
    }
}