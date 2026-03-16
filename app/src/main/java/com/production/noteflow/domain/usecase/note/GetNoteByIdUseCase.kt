package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteId: String) = noteRepository.getNoteById(noteId)
}