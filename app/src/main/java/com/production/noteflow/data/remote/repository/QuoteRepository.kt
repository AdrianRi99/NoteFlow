package com.production.noteflow.data.remote.repository

import android.util.Log
import com.production.noteflow.data.remote.QuoteApiService
import com.production.noteflow.data.remote.dto.toDomain
import com.production.noteflow.domain.Quote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(
    private val quoteApiService: QuoteApiService
) {
    suspend fun getRandomQuote(): Quote {

        Log.d("waaa", quoteApiService.debugRandomQuote())
        return quoteApiService.getRandomQuote().toDomain()
    }
}