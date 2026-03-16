package com.production.noteflow.service.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        val reminderId = intent.getStringExtra(EXTRA_REMINDER_ID) ?: run {
            pendingResult.finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val entryPoint = EntryPointAccessors.fromApplication(
                    context,
                    ReminderReceiverEntryPoint::class.java
                )

                val reminderRepository = entryPoint.reminderRepository()
                val noteRepository = entryPoint.noteRepository()
                val scheduler = entryPoint.reminderScheduler()

                val reminder = reminderRepository.getReminderById(reminderId)
                if (reminder != null && reminder.enabled) {
                    val note = noteRepository.getNoteByIdOnce(reminder.noteId)

                    NotificationHelper.showReminder(
                        context = context,
                        notificationId = reminder.id.hashCode(),
                        noteId = reminder.noteId,
                        title = note?.title ?: "Reminder",
                        text = note?.subtitle?.takeIf { it.isNotBlank() } ?: "Zeit für deine Notiz"
                    )

                    scheduler.schedule(reminder)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}