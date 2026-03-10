package com.production.noteflow.di

import com.production.noteflow.data.local.daos.NoteDao
import com.production.noteflow.data.local.daos.ReminderDao
import com.production.noteflow.data.repository.NoteRepository
import com.production.noteflow.data.repository.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao
    ): NoteRepository {
        return NoteRepository(noteDao)
    }

    @Provides
    @Singleton
    fun provideReminderRepository(reminderDao: ReminderDao): ReminderRepository {
        return ReminderRepository(reminderDao)
    }
}