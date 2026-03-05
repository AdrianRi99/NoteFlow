package com.production.noteflow.data

import com.production.noteflow.model.UiItem

object SampleData {

    fun sampleItems(): List<UiItem> {
        return listOf(
            UiItem(
                id = "1",
                title = "Projekt-Notizen",
                subtitle = "Compose Layout + State lernen",
                tag = "Work"
            ),
            UiItem(
                id = "2",
                title = "Einkaufsliste",
                subtitle = "Milch, Brot, Obst",
                tag = "Home"
            ),
            UiItem(
                id = "3",
                title = "Workout",
                subtitle = "30 Minuten Cardio",
                tag = "Health"
            ),
            UiItem(
                id = "4",
                title = "Leseliste",
                subtitle = "Kotlin Kapitel 5",
                tag = "Study"
            )
        )
    }

}