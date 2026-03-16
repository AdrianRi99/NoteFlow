package com.production.noteflow.data.remote.dto

import kotlinx.serialization.Serializable
import com.production.noteflow.domain.model.Quote

@Serializable
data class QuoteDto(
    val id: Int,
    val quote: String,
    val author: String
)

fun QuoteDto.toDomain(): Quote {
    return Quote(
        text = quote,
        author = author
    )
}