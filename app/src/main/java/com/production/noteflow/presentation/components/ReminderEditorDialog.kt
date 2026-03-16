package com.production.noteflow.presentation.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.production.noteflow.presentation.model.ReminderDraft
import com.production.noteflow.presentation.model.Weekdays

@Composable
fun ReminderEditorDialog(
    existing: List<ReminderDraft>,
    onDismiss: () -> Unit,
    onSave: (List<ReminderDraft>) -> Unit
) {
    val context = LocalContext.current

    val initial = remember(existing) {
        Weekdays.all.map { (day, _) ->
            val existingForDay = existing.firstOrNull { it.dayOfWeek == day }
            ReminderDraft(
                dayOfWeek = day,
                enabled = existingForDay != null,
                hour = existingForDay?.hour ?: 9,
                minute = existingForDay?.minute ?: 0
            )
        }
    }

    var drafts by remember { mutableStateOf(initial) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reminder bearbeiten") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Weekdays.all.forEach { (day, label) ->
                    val draft = drafts.first { it.dayOfWeek == day }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = draft.enabled,
                            onClick = {
                                drafts = drafts.map {
                                    if (it.dayOfWeek == day) it.copy(enabled = !it.enabled) else it
                                }
                            },
                            label = { Text(label) }
                        )

                        OutlinedButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        drafts = drafts.map {
                                            if (it.dayOfWeek == day) {
                                                it.copy(hour = hour, minute = minute)
                                            } else it
                                        }
                                    },
                                    draft.hour,
                                    draft.minute,
                                    true
                                ).show()
                            },
                            enabled = draft.enabled
                        ) {
                            Text("%02d:%02d".format(draft.hour, draft.minute))
                        }
                    }
                }

                Text(
                    text = "Du kannst mehrere Tage aktivieren. Jeder Tag hat seine eigene Uhrzeit.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(drafts.filter { it.enabled })
                }
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}