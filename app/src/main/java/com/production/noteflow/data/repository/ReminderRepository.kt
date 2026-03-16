package com.production.noteflow.data.repository

import com.production.noteflow.data.local.room.daos.ReminderDao
import com.production.noteflow.data.local.room.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) {
    fun getRemindersForNote(noteId: String): Flow<List<ReminderEntity>> =
        reminderDao.getRemindersForNote(noteId)

    suspend fun getRemindersForNoteOnce(noteId: String): List<ReminderEntity> =
        reminderDao.getRemindersForNoteOnce(noteId)

    suspend fun getReminderById(id: String): ReminderEntity? =
        reminderDao.getReminderById(id)

    suspend fun replaceRemindersForNote(
        noteId: String,
        reminders: List<ReminderEntity>
    ) {
        reminderDao.deleteRemindersForNote(noteId)
        if (reminders.isNotEmpty()) {
            reminderDao.insertReminders(reminders)
        }
    }

    suspend fun deleteReminderById(id: String) {
        reminderDao.deleteReminderById(id)
    }
}