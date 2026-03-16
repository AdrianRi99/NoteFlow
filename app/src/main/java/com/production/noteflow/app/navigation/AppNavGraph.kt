package com.production.noteflow.app.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.production.noteflow.app.navigation.Routes
import com.production.noteflow.presentation.screen.create.CreateNoteScreen
import com.production.noteflow.presentation.screen.create.CreateNoteViewModel
import com.production.noteflow.presentation.screen.dashboard.DashboardScreen
import com.production.noteflow.presentation.screen.dashboard.DashboardViewModel
import com.production.noteflow.presentation.screen.detail.NoteDetailScreen
import com.production.noteflow.presentation.screen.detail.NoteDetailViewModel
import com.production.noteflow.presentation.screen.edit.EditNoteScreen
import com.production.noteflow.presentation.screen.edit.EditNoteViewModel

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
            val quoteUiState by viewModel.quoteUiState.collectAsStateWithLifecycle()

            DashboardScreen (
                items = notes,
                quoteUiState = quoteUiState,
                onRefreshQuote = viewModel::refreshQuote,
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
                onBack = { navController.popBackStack() },
                onEdit = {
                    val noteId = uiState.note?.id ?: return@NoteDetailScreen
                    navController.navigate("${Routes.EDIT_NOTE}/$noteId")
                },
                onDelete = {
                    viewModel.deleteCurrentNote {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(
            route = "${Routes.EDIT_NOTE}/{${Routes.NOTE_ID}}",
            arguments = listOf(navArgument(Routes.NOTE_ID) { type = NavType.StringType })
        ) {
            val viewModel: EditNoteViewModel = hiltViewModel()

            EditNoteScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                onDeleted = {
                    navController.popBackStack(Routes.DASHBOARD, false)
                }
            )
        }

    }
}
