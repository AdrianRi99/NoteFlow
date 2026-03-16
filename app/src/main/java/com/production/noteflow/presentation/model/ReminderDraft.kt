package com.production.noteflow.presentation.model

data class ReminderDraft(
    val dayOfWeek: Int,
    val enabled: Boolean,
    val hour: Int,
    val minute: Int
)