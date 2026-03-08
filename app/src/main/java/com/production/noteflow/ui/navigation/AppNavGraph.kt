package com.production.noteflow.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.production.noteflow.ui.screen.create.CreateNoteScreen
import com.production.noteflow.ui.screen.create.CreateNoteViewModel
import com.production.noteflow.ui.screen.dashboard.DashboardScreen
import com.production.noteflow.ui.screen.dashboard.DashboardViewModel
import com.production.noteflow.ui.screen.detail.NoteDetailScreen
import com.production.noteflow.ui.screen.detail.NoteDetailViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = hiltViewModel()
            val notes by viewModel.notes.collectAsStateWithLifecycle()

            DashboardScreen (
                items = notes,
                onAddClick = {
                    navController.navigate(Routes.CREATE_NOTE)
                },
                onItemClick = { note ->
                    navController.navigate("${Routes.NOTE_DETAIL}/${note.id}")
                }
            )
        }

        composable(Routes.CREATE_NOTE) {
            val viewModel: CreateNoteViewModel = hiltViewModel()

            CreateNoteScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.NOTE_DETAIL}/{${Routes.NOTE_ID}}",
            arguments = listOf(
                navArgument(Routes.NOTE_ID) { type = NavType.StringType }
            )
        ) {
            val viewModel: NoteDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            NoteDetailScreen(
                uiState = uiState,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
