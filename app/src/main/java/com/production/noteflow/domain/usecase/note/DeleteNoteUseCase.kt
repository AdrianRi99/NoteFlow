package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.repository.NoteRepository
import com.production.noteflow.domain.repository.ReminderRepository
import com.production.noteflow.domain.scheduler.ReminderScheduler
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(note: Note): Result<Unit> {
        return runCatching {
            val reminders = reminderRepository.getRemindersForNoteOnce(note.id)
            reminders.forEach(reminderScheduler::cancel)
            noteRepository.deleteNote(note)
        }
    }
}