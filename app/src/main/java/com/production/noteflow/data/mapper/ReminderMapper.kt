package com.production.noteflow.data.mapper

import com.production.noteflow.data.local.room.entities.ReminderEntity
import com.production.noteflow.domain.model.Reminder

fun ReminderEntity.toDomain(): Reminder {
    return Reminder(
        id = id,
        noteId = noteId,
        dayOfWeek = dayOfWeek,
        hour = hour,
        minute = minute,
        enabled = enabled
    )
}

fun Reminder.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        noteId = noteId,
        dayOfWeek = dayOfWeek,
        hour = hour,
        minute = minute,
        enabled = enabled
    )
}