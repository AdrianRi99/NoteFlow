package com.production.noteflow.domain

data class Quote(
    val text: String,
    val author: String
)

data class StoredQuote(
    val text: String,
    val author: String,
    val lastUpdatedMillis: Long
)