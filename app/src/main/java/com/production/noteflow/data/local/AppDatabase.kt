package com.production.noteflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.production.noteflow.data.local.daos.NoteDao
import com.production.noteflow.data.local.daos.ReminderDao
import com.production.noteflow.data.local.entities.NoteEntity
import com.production.noteflow.data.local.entities.ReminderEntity

@Database(
    entities = [NoteEntity::class, ReminderEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun reminderDao(): ReminderDao

}