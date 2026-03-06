package com.production.noteflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.production.noteflow.data.SampleData.sampleItems
import com.production.noteflow.model.UiItem
import com.production.noteflow.ui.screen.dashboard.DashboardScreen
import com.production.noteflow.ui.screen.detail.NoteDetailScreen

@Composable
fun AppNavGraph(
    items: List<UiItem> = sampleItems()
) {
    val navController = rememberNavController()

    // schneller Lookup für Details
    val itemById = remember(items) { items.associateBy { it.id } }

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen (
                items = items,
                onAddClick = { /* TODO */ },
                onItemClick = { item ->
                    navController.navigate("${Routes.NOTE_DETAIL}/${item.id}")
                }
            )
        }

        composable(
            route = "${Routes.NOTE_DETAIL}/{${Routes.NOTE_ID}}",
            arguments = listOf(navArgument(Routes.NOTE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Routes.NOTE_ID).orEmpty()
            val item = itemById[id]

            NoteDetailScreen(
                item = item,
                onBack = { navController.popBackStack() }
            )
        }
    }
}