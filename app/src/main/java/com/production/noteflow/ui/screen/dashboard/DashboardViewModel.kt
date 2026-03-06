package com.production.noteflow.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.production.noteflow.data.local.NoteEntity
import com.production.noteflow.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class DashboardViewModel(
    repository: NoteRepository
) : ViewModel() {
    val notes: Flow<List<NoteEntity>> = repository.getNotes()
}

class DashboardViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository) as T
    }
}