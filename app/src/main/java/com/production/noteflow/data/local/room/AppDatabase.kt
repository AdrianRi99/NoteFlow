package com.production.noteflow.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.production.noteflow.data.local.room.daos.NoteDao
import com.production.noteflow.data.local.room.entities.NoteEntity
import com.production.noteflow.data.local.room.daos.ReminderDao
import com.production.noteflow.data.local.room.entities.ReminderEntity

@Database(
    entities = [NoteEntity::class, ReminderEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun reminderDao(): ReminderDao

}