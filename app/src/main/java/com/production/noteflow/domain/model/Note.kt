package com.production.noteflow.domain.model

data class Note(
    val id: String,
    val title: String,
    val subtitle: String,
    val tag: String,
    val content: String,
    val createdAt: Long,
    val imageUri: String? = null
)