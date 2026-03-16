package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.NoteDetail
import com.production.noteflow.domain.repository.NoteRepository
import com.production.noteflow.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetNoteDetailUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(noteId: String) =
        combine(
            noteRepository.getNoteById(noteId),
            reminderRepository.getRemindersForNote(noteId)
        ) { note, reminders ->
            NoteDetail(
                note = note,
                reminders = reminders
            )
        }
}