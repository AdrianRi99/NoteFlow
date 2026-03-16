package com.production.noteflow.data.repository

import com.production.noteflow.data.local.room.daos.ReminderDao
import com.production.noteflow.data.mapper.toDomain
import com.production.noteflow.data.mapper.toEntity
import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override fun getRemindersForNote(noteId: String): Flow<List<Reminder>> {
        return reminderDao.getRemindersForNote(noteId)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getRemindersForNoteOnce(noteId: String): List<Reminder> {
        return reminderDao.getRemindersForNoteOnce(noteId).map { it.toDomain() }
    }

    override suspend fun getReminderById(id: String): Reminder? {
        return reminderDao.getReminderById(id)?.toDomain()
    }

    override suspend fun replaceRemindersForNote(noteId: String, reminders: List<Reminder>) {
        reminderDao.deleteRemindersForNote(noteId)
        if (reminders.isNotEmpty()) {
            reminderDao.insertReminders(reminders.map { it.toEntity() })
        }
    }

    override suspend fun deleteReminderById(id: String) {
        reminderDao.deleteReminderById(id)
    }
}