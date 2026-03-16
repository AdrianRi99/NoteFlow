package com.production.noteflow.service.reminder

import com.production.noteflow.domain.repository.NoteRepository
import com.production.noteflow.domain.repository.ReminderRepository
import com.production.noteflow.domain.scheduler.ReminderScheduler
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