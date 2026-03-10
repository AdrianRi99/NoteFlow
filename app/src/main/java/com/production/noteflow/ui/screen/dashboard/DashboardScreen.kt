package com.production.noteflow.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.production.noteflow.data.local.entities.NoteEntity
import com.production.noteflow.ui.components.ItemCard
import com.production.noteflow.ui.components.StatCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    items: List<NoteEntity>,
    onAddClick: () -> Unit = {},
    onItemClick: (NoteEntity) -> Unit = {},
) {
    var query by rememberSaveable { mutableStateOf("") }
    //rememberSaveable → überlebt Rotation
    var selectedTag by rememberSaveable { mutableStateOf("All") }

    val tags = remember(items) { listOf("All") + items.map { it.tag }.distinct() }
    val filtered = remember(query, selectedTag, items) {
        items.filter { item ->
            (selectedTag == "All" || item.tag == selectedTag) &&
                    (query.isBlank() || item.title.contains(query, true) || item.subtitle.contains(
                        query,
                        true
                    ))
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dashboard") },
//                actions = {
//                    IconButton(onClick = { /* TODO */ }) {
//                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
//                    }
//                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Willkommen zurück 👋",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Erstelle und organisiere deine Notizen übersichtlich an einem Ort.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            placeholder = { Text("Suche…") }
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.take(4).forEach { tag ->
                        FilterChip(
                            selected = selectedTag == tag,
                            onClick = { selectedTag = tag },
                            label = { Text(tag) }
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Items",
                        value = filtered.size.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Tag",
                        value = selectedTag,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    "Aktuell",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(filtered, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }

            item {
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DashboardPreview() {
//    NoteFlowTheme {
//        DashboardScreen()
//    }
//}