package com.production.noteflow.domain

data class ReminderDraft(
    val dayOfWeek: Int,
    val enabled: Boolean,
    val hour: Int,
    val minute: Int
)