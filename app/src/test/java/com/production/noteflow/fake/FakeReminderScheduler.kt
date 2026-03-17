package com.production.noteflow.fake

import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.scheduler.ReminderScheduler

class FakeReminderScheduler : ReminderScheduler {
    val scheduled = mutableListOf<Reminder>()
    val cancelled = mutableListOf<Reminder>()

    override fun schedule(reminder: Reminder) {
        scheduled += reminder
    }

    override fun cancel(reminder: Reminder) {
        cancelled += reminder
    }
}