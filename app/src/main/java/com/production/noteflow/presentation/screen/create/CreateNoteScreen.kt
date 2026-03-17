package com.production.noteflow.presentation.screen.create

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.production.noteflow.presentation.components.NoteEditorContent
import com.production.noteflow.presentation.model.NoteEditorEvent
import com.production.noteflow.presentation.model.NoteEditorMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    viewModel: CreateNoteViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is NoteEditorEvent.Saved -> onSaved()
                is NoteEditorEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is NoteEditorEvent.Deleted -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = viewModel::saveNote,
                icon = { Icon(Icons.Default.Save, contentDescription = null) },
                text = { Text("Save") }
            )
        }
    ) { padding ->
        NoteEditorContent(
            mode = NoteEditorMode.Create,
            title = uiState.title,
            subtitle = uiState.subtitle,
            content = uiState.content,
            selectedTag = uiState.selectedTag,
            selectedImageUri = uiState.selectedImageUri,
            reminders = uiState.reminders,
            onTitleChange = viewModel::onTitleChange,
            onSubtitleChange = viewModel::onSubtitleChange,
            onContentChange = viewModel::onContentChange,
            onTagChange = viewModel::onTagChange,
            onImageSelected = viewModel::onImageSelected,
            onRemoveImage = viewModel::removeImage,
            onToggleDay = viewModel::toggleReminderDay,
            onTimeChange = viewModel::updateReminderTime,
            modifier = Modifier.padding(padding)
        )
    }
}