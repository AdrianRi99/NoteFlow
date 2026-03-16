package com.production.noteflow.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("noteId")]
)
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val noteId: String,
    val dayOfWeek: Int, // 1 = Monday ... 7 = Sunday
    val hour: Int,
    val minute: Int,
    val enabled: Boolean = true
)