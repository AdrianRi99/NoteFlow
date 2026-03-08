package com.production.noteflow.ui.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    uiState: NoteDetailUiState,
    onBack: () -> Unit,
) {
    val note = uiState.note

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = note?.title ?: "Notiz",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO Edit */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Bearbeiten")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO Reminder */ },
                icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                text = { Text("Reminder") }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            note == null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Diese Notiz wurde nicht gefunden.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(end = 88.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = note.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(6.dp))
                                AssistChip(
                                    onClick = {},
                                    label = { Text(note.tag) }
                                )
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text("Status", style = MaterialTheme.typography.labelLarge)
                                    Text("Saved", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }

                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Inhalt", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = note.content.ifBlank { "Kein Inhalt vorhanden." },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun NoteDetailScreenPreview() {
//
//    val sampleItem = UiItem(
//        id = "1",
//        title = "Compose Navigation",
//        subtitle = "Understanding how navigation works in Jetpack Compose.",
//        tag = "Android"
//    )
//
//    MaterialTheme {
//        NoteDetailScreen(
//            item = sampleItem,
//            onBack = {}
//        )
//    }
//}