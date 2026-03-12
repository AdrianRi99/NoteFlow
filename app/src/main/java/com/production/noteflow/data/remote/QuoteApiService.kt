package com.production.noteflow.data.remote

import com.production.noteflow.data.remote.dto.QuoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getRandomQuote(): QuoteDto {
        return httpClient
            .get("https://dummyjson.com/quotes/random")
            .body()
    }

//    suspend fun debugRandomQuote(): String {
//        return httpClient
//            .get("https://dummyjson.com/quotes/random")
//            .bodyAsText()
//    }
}