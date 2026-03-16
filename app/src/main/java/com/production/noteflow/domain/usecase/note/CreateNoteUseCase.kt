package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.model.Reminder
import com.production.noteflow.domain.model.ReminderDraft
import com.production.noteflow.domain.repository.NoteRepository
import com.production.noteflow.domain.repository.ReminderRepository
import com.production.noteflow.domain.scheduler.ReminderScheduler
import java.util.UUID
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(
        title: String,
        subtitle: String,
        content: String,
        tag: String,
        imageUri: String?,
        reminders: List<ReminderDraft>
    ): Result<Unit> {
        return runCatching {
            require(title.isNotBlank()) { "Titel darf nicht leer sein." }

            val noteId = UUID.randomUUID().toString()

            val note = Note(
                id = noteId,
                title = title.trim(),
                subtitle = subtitle.trim(),
                tag = tag,
                content = content.trim(),
                createdAt = System.currentTimeMillis(),
                imageUri = imageUri
            )

            noteRepository.insertNote(note)

            val reminderModels = reminders
                .filter { it.enabled }
                .map {
                    Reminder(
                        id = UUID.randomUUID().toString(),
                        noteId = noteId,
                        dayOfWeek = it.dayOfWeek,
                        hour = it.hour,
                        minute = it.minute,
                        enabled = true
                    )
                }

            if (reminderModels.isNotEmpty()) {
                reminderRepository.replaceRemindersForNote(noteId, reminderModels)
                reminderModels.forEach(reminderScheduler::schedule)
            }
        }
    }
}