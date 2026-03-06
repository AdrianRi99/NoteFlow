package com.production.noteflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.production.noteflow.data.SampleData.sampleItems
import com.production.noteflow.data.local.toUiItem
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.model.UiItem
import com.production.noteflow.ui.screen.create.CreateNoteScreen
import com.production.noteflow.ui.screen.create.CreateNoteViewModel
import com.production.noteflow.ui.screen.create.CreateNoteViewModelFactory
import com.production.noteflow.ui.screen.dashboard.DashboardScreen
import com.production.noteflow.ui.screen.dashboard.DashboardViewModel
import com.production.noteflow.ui.screen.dashboard.DashboardViewModelFactory
import com.production.noteflow.ui.screen.detail.NoteDetailScreen
import kotlin.collections.emptyList

@Composable
fun AppNavGraph(
//    items: List<UiItem> = sampleItems()
    repository: NoteRepository
) {
    val navController = rememberNavController()

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(repository)
    )
    val notes by dashboardViewModel.notes.collectAsState(initial = emptyList())

    val createViewModel: CreateNoteViewModel = viewModel(
        factory = CreateNoteViewModelFactory(repository)
    )

    // schneller Lookup für Details
    val itemById = remember(notes) { notes.associateBy { it.id } }

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen (
                items = notes.map { it.toUiItem() },
                onAddClick = {
                    navController.navigate(Routes.CREATE_NOTE)
                },
                onItemClick = { item ->
                    navController.navigate("${Routes.NOTE_DETAIL}/${item.id}")
                }
            )
        }

        composable(Routes.CREATE_NOTE) {
            CreateNoteScreen(
                viewModel = createViewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.NOTE_DETAIL}/{${Routes.NOTE_ID}}",
            arguments = listOf(navArgument(Routes.NOTE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Routes.NOTE_ID).orEmpty()
            val item = itemById[id]

            NoteDetailScreen(
                item = item?.toUiItem(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}