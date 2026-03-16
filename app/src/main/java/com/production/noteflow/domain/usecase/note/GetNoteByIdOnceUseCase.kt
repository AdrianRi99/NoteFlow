package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.model.Note
import com.production.noteflow.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdOnceUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: String): Note? {
        return noteRepository.getNoteByIdOnce(noteId)
    }
}