package com.production.noteflow.fake

import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeReminderRepository : ReminderRepository {

    private val remindersFlow = MutableStateFlow<List<Reminder>>(emptyList())

    override fun getRemindersForNote(noteId: String): Flow<List<Reminder>> {
        return remindersFlow.map { reminders ->
            reminders.filter { it.noteId == noteId }
        }
    }

    override suspend fun getRemindersForNoteOnce(noteId: String): List<Reminder> {
        return remindersFlow.value.filter { it.noteId == noteId }
    }

    override suspend fun getReminderById(id: String): Reminder? {
        return remindersFlow.value.firstOrNull { it.id == id }
    }

    override suspend fun replaceRemindersForNote(noteId: String, reminders: List<Reminder>) {
        remindersFlow.value =
            remindersFlow.value.filterNot { it.noteId == noteId } + reminders
    }

    override suspend fun deleteReminderById(id: String) {
        remindersFlow.value = remindersFlow.value.filterNot { it.id == id }
    }

    fun seed(reminders: List<Reminder>) {
        remindersFlow.value = reminders
    }
}