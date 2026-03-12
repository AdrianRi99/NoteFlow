package com.production.noteflow.services.reminder

import com.production.noteflow.data.local.repository.NoteRepository
import com.production.noteflow.data.local.repository.ReminderRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderReceiverEntryPoint {
    fun reminderRepository(): ReminderRepository
    fun noteRepository(): NoteRepository
    fun reminderScheduler(): ReminderScheduler
}