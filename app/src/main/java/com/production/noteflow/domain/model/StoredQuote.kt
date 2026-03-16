package com.production.noteflow.domain.model

data class StoredQuote(
    val text: String,
    val author: String,
    val lastUpdatedMillis: Long
)