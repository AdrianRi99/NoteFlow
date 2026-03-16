package com.production.noteflow.presentation.model

data class Weekday(
    val value: Int,
    val short: String,
    val full: String
)

object Weekdays {

    val all = listOf(
        Weekday(1, "Mo", "Montag"),
        Weekday(2, "Di", "Dienstag"),
        Weekday(3, "Mi", "Mittwoch"),
        Weekday(4, "Do", "Donnerstag"),
        Weekday(5, "Fr", "Freitag"),
        Weekday(6, "Sa", "Samstag"),
        Weekday(7, "So", "Sonntag")
    )
}
