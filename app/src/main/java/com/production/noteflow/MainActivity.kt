package com.production.noteflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.production.noteflow.ui.navigation.AppNavGraph
import com.production.noteflow.ui.theme.NoteFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java,
//            "notes_db"
//        ).build()
//
//        val repository = NoteRepository(db.noteDao())

        setContent {
            NoteFlowTheme {
//                AppNavGraph(repository = repository)
                AppNavGraph()
            }
        }
    }
}
