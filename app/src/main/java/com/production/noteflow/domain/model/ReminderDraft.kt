package com.production.noteflow.domain.model

data class ReminderDraft(
    val dayOfWeek: Int,
    val enabled: Boolean,
    val hour: Int,
    val minute: Int
)

object ReminderDraftFactory {
    fun defaultReminderDrafts() = listOf(
        ReminderDraft(1, false, 9, 0),
        ReminderDraft(2, false, 9, 0),
        ReminderDraft(3, false, 9, 0),
        ReminderDraft(4, false, 9, 0),
        ReminderDraft(5, false, 9, 0),
        ReminderDraft(6, false, 9, 0),
        ReminderDraft(7, false, 9, 0)
    )
}