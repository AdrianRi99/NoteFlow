package com.production.noteflow.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val tag: String,
    val content: String,
    val createdAt: Long,
    val imageUri: String? = null
)