package com.production.noteflow.domain.repository

import com.production.noteflow.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getRemindersForNote(noteId: String): Flow<List<Reminder>>
    suspend fun getRemindersForNoteOnce(noteId: String): List<Reminder>
    suspend fun getReminderById(id: String): Reminder?
    suspend fun replaceRemindersForNote(noteId: String, reminders: List<Reminder>)
    suspend fun deleteReminderById(id: String)
}