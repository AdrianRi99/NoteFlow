package com.production.noteflow.domain.model

data class Reminder(
    val id: String,
    val noteId: String,
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int,
    val enabled: Boolean = true
)