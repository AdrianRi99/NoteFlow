package com.production.noteflow.presentation.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.production.noteflow.domain.model.ReminderDraft
import com.production.noteflow.presentation.model.Weekdays

@Composable
fun ReminderEditorCard(
    reminders: List<ReminderDraft>,
    onToggleDay: (Int) -> Unit,
    onTimeChange: (dayOfWeek: Int, hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Reminder",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Weekdays.all) { (day, label) ->
                    val reminder = reminders.firstOrNull { it.dayOfWeek == day }
                    val selected = reminder?.enabled == true

                    FilterChip(
                        selected = selected,
                        onClick = { onToggleDay(day) },
                        label = { Text(label) }
                    )
                }
            }

            reminders
                .filter { it.enabled }
                .sortedBy { it.dayOfWeek }
                .forEach { reminder ->

                    val label = Weekdays.all
                        .first { it.value == reminder.dayOfWeek }
                        .full

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall
                        )

                        OutlinedButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        onTimeChange(reminder.dayOfWeek, hour, minute)
                                    },
                                    reminder.hour,
                                    reminder.minute,
                                    true
                                ).show()
                            }
                        ) {
                            Text("%02d:%02d".format(reminder.hour, reminder.minute))
                        }
                    }
                }

            if (reminders.none { it.enabled }) {
                Text(
                    text = "Keine Reminder ausgewählt.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}