package com.production.noteflow.domain.usecase.reminder

import com.production.noteflow.domain.repository.ReminderRepository
import javax.inject.Inject

class GetRemindersForNoteUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(noteId: String) = reminderRepository.getRemindersForNote(noteId)
}