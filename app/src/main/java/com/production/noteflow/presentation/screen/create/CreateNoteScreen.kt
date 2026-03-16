package com.production.noteflow.presentation.screen.create

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.production.noteflow.domain.constants.NoteTags
import com.production.noteflow.presentation.components.ReminderEditorCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    viewModel: CreateNoteViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    val context = LocalContext.current
    val tags = NoteTags.default

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // nicht jeder Provider erlaubt persistierbare Rechte
            }

            viewModel.onImageSelected(uri.toString())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Neue Notiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.saveNote(onSaved) },
                icon = { Icon(Icons.Default.Save, contentDescription = null) },
                text = { Text("Speichern") }
            )
        }
    ) { padding ->
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = viewModel.selectedTag,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (viewModel.selectedImageUri != null) {
                            Box(
                                modifier = Modifier.size(104.dp)
                            ) {
                                AsyncImage(
                                    model = viewModel.selectedImageUri,
                                    contentDescription = "Ausgewähltes Bild",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable {
                                            pickMedia.launch(
                                                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                                            )
                                        }
                                )

                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    IconButton(
                                        onClick = viewModel::removeImage,
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Bild entfernen"
                                        )
                                    }
                                }
                            }
                        } else {

                            Surface(
                                modifier = Modifier
                                    .size(104.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        pickMedia.launch(
                                            PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                                        )
                                    },
                                shape = RoundedCornerShape(16.dp),
                                color = Color.Transparent
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.AddPhotoAlternate,
                                        contentDescription = "Bild auswählen",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            OutlinedTextField(
                                value = viewModel.title,
                                onValueChange = viewModel::onTitleChange,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Titel") },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = viewModel.subtitle,
                                onValueChange = viewModel::onSubtitleChange,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Kurzbeschreibung") },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    }
                }
            }

            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Kategorie",
                        style = MaterialTheme.typography.titleMedium
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(tags) { tag ->
                            FilterChip(
                                selected = viewModel.selectedTag == tag,
                                onClick = { viewModel.onTagChange(tag) },
                                label = { Text(tag) }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = viewModel.content,
                onValueChange = viewModel::onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                label = { Text("Inhalt") },
                shape = RoundedCornerShape(16.dp)
            )

            ReminderEditorCard(
                reminders = viewModel.reminders,
                onToggleDay = viewModel::toggleReminderDay,
                onTimeChange = viewModel::updateReminderTime
            )

            Spacer(Modifier.height(96.dp))
        }
    }
}