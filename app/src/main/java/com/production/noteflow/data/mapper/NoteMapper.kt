package com.production.noteflow.data.mapper

import com.production.noteflow.data.local.room.entities.NoteEntity
import com.production.noteflow.domain.model.Note

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        subtitle = subtitle,
        tag = tag,
        content = content,
        createdAt = createdAt,
        imageUri = imageUri
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        subtitle = subtitle,
        tag = tag,
        content = content,
        createdAt = createdAt,
        imageUri = imageUri
    )
}