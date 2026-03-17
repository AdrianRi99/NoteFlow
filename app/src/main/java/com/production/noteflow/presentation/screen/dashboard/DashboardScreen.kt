package com.production.noteflow.presentation.screen.dashboard

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.production.noteflow.domain.model.Note
import com.production.noteflow.presentation.components.ItemCard
import com.production.noteflow.presentation.components.StatCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    items: List<Note>,
    quoteUiState: QuoteUiState,
    onRefreshQuote: () -> Unit,
    onAddClick: () -> Unit = {},
    onItemClick: (Note) -> Unit = {},
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
                val pagerState = rememberPagerState(pageCount = { 2 })

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        when (page) {
                            0 -> {
                                ElevatedCard(
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Welcome back 👋",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(
                                            text = "Create and organize your notes clearly in one place.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        OutlinedTextField(
                                            value = query,
                                            onValueChange = { query = it },
                                            modifier = Modifier
                                                .padding(top = 10.dp)
                                                .fillMaxWidth(),
                                            singleLine = true,
                                            shape = RoundedCornerShape(14.dp),
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Search,
                                                    contentDescription = null
                                                )
                                            },
                                            placeholder = { Text("Search…") }
                                        )
                                    }
                                }
                            }

                            1 -> {
                                ElevatedCard(
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Daily Inspiration 📜",
                                                style = MaterialTheme.typography.titleLarge
                                            )

                                            IconButton(
                                                onClick = onRefreshQuote
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Refresh,
                                                    contentDescription = "Neues Zitat"
                                                )
                                            }
                                        }

                                        when {
                                            quoteUiState.isLoading -> {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.padding(top = 8.dp)
                                                )
                                            }

                                            quoteUiState.quote != null -> {
                                                Text(
                                                    text = "“${quoteUiState.quote.text}”",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontStyle = FontStyle.Italic,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )

                                                Text(
                                                    text = "by ${quoteUiState.quote.author}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            else -> {
                                                Text(
                                                    text = quoteUiState.errorMessage ?: "No quote available",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(2) { index ->
                            val isSelected = pagerState.currentPage == index

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(if (isSelected) 10.dp else 8.dp)
                                    .background(
                                        color = if (isSelected) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
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
                    "Recent",
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