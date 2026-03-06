package com.production.noteflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.production.noteflow.data.local.AppDatabase
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.ui.navigation.AppNavGraph
import com.production.noteflow.ui.screen.dashboard.DashboardScreen
import com.production.noteflow.ui.theme.NoteFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes_db"
        ).build()

        val repository = NoteRepository(db.noteDao())

        setContent {
            NoteFlowTheme {
                AppNavGraph(repository = repository)
            }
        }
    }
}
