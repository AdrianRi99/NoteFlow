package com.production.noteflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.production.noteflow.model.UiItem

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val tag: String,
    val content: String,
    val createdAt: Long,
)
