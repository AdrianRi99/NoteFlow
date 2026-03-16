package com.production.noteflow.domain.model

data class NoteDetail(
    val note: Note?,
    val reminders: List<Reminder>
)