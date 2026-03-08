package com.production.noteflow.di

import com.production.noteflow.data.local.NoteDao
import com.production.noteflow.data.repository.NoteRepository
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
}