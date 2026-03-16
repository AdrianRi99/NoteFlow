package com.production.noteflow.domain.scheduler

import com.production.noteflow.domain.model.Reminder

interface ReminderScheduler {
    fun schedule(reminder: Reminder)
    fun cancel(reminder: Reminder)
}