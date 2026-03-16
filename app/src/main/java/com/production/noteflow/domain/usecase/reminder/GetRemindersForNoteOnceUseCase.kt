package com.production.noteflow.domain.usecase.reminder

import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.repository.ReminderRepository
import javax.inject.Inject

class GetRemindersForNoteOnceUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(noteId: String): List<Reminder> {
        return reminderRepository.getRemindersForNoteOnce(noteId)
    }
}