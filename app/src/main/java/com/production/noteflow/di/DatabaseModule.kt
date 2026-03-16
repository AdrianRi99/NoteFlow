package com.production.noteflow.di

import android.content.Context
import androidx.room.Room
import com.production.noteflow.data.local.room.AppDatabase
import com.production.noteflow.data.local.room.daos.NoteDao
import com.production.noteflow.data.local.room.daos.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "notes_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao = database.reminderDao()


}