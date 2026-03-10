package com.production.noteflow.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.production.noteflow.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE noteId = :noteId ORDER BY dayOfWeek, hour, minute")
    fun getRemindersForNote(noteId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE noteId = :noteId ORDER BY dayOfWeek, hour, minute")
    suspend fun getRemindersForNoteOnce(noteId: String): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE id = :id LIMIT 1")
    suspend fun getReminderById(id: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderEntity>)

    @Query("DELETE FROM reminders WHERE noteId = :noteId")
    suspend fun deleteRemindersForNote(noteId: String)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: String)
}