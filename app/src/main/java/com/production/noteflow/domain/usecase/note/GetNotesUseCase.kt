package com.production.noteflow.domain.usecase.note

import com.production.noteflow.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke() = noteRepository.getNotes()
}