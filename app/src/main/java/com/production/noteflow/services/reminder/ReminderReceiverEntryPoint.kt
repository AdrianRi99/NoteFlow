package com.production.noteflow.services.reminder

import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.data.repository.ReminderRepository
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