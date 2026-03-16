package com.production.noteflow.presentation.screen.detail

//import com.production.noteflow.data.local.room.entities.NoteEntity
//import com.production.noteflow.data.local.room.entities.ReminderEntity
//
//data class NoteDetailUiState(
//    val isLoading: Boolean = true,
//    val note: NoteEntity? = null,
//    val reminders: List<ReminderEntity> = emptyList()
//)


import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.Reminder

data class NoteDetailUiState(
    val isLoading: Boolean = true,
    val note: Note? = null,
    val reminders: List<Reminder> = emptyList()
)