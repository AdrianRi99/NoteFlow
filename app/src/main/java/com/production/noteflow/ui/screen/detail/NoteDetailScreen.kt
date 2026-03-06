package com.production.noteflow.ui.screen.detail

import com.production.noteflow.model.UiItem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    item: UiItem?,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        item?.title ?: "Notiz",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO edit */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO reminder */ },
                icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                text = { Text("Reminder") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // HERO-HEADER mit Box (wichtig: Box Layout)
            ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // links: Inhalt
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(end = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item?.title ?: "Notiz nicht gefunden",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = item?.subtitle
                                ?: "Diese Notiz existiert nicht (mehr) oder wurde noch nicht geladen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(6.dp))
                        AssistChip(
                            onClick = { /* optional */ },
                            label = { Text(item?.tag ?: "Unknown") }
                        )
                    }

                    // rechts: "Status Kachel" (Box Overlay)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.End) {
                            Text("Status", style = MaterialTheme.typography.labelLarge)
                            Text("Draft", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

            // CONTENT CARD (hier würdest du später echten Notiz-Text zeigen)
            ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Inhalt", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Hier kommt später der vollständige Notiztext hin. " +
                                "Du kannst das mit Room speichern und im Detail anzeigen.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // QUICK ACTIONS
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                ) { Text("Teilen") }

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                ) { Text("Bearbeiten") }
            }

            Spacer(Modifier.height(80.dp)) // Platz für FAB
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoteDetailScreenPreview() {

    val sampleItem = UiItem(
        id = "1",
        title = "Compose Navigation",
        subtitle = "Understanding how navigation works in Jetpack Compose.",
        tag = "Android"
    )

    MaterialTheme {
        NoteDetailScreen(
            item = sampleItem,
            onBack = {}
        )
    }
}