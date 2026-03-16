package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.model.ReminderDraft
import com.production.noteflow.domain.repository.NoteRepository
import com.production.noteflow.domain.repository.ReminderRepository
import com.production.noteflow.domain.scheduler.ReminderScheduler
import java.util.UUID
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(
        originalNote: Note,
        title: String,
        subtitle: String,
        content: String,
        tag: String,
        imageUri: String?,
        reminders: List<ReminderDraft>
    ): Result<Unit> {
        return runCatching {
            require(title.isNotBlank()) { "Titel darf nicht leer sein." }

            val updatedNote = originalNote.copy(
                title = title.trim(),
                subtitle = subtitle.trim(),
                content = content.trim(),
                tag = tag,
                imageUri = imageUri
            )

            noteRepository.updateNote(updatedNote)

            val oldReminders = reminderRepository.getRemindersForNoteOnce(originalNote.id)
            oldReminders.forEach(reminderScheduler::cancel)

            val newReminders = reminders
                .filter { it.enabled }
                .map {
                    Reminder(
                        id = UUID.randomUUID().toString(),
                        noteId = originalNote.id,
                        dayOfWeek = it.dayOfWeek,
                        hour = it.hour,
                        minute = it.minute,
                        enabled = true
                    )
                }

            reminderRepository.replaceRemindersForNote(originalNote.id, newReminders)
            newReminders.forEach(reminderScheduler::schedule)
        }
    }
}