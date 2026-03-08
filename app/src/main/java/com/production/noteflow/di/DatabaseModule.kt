package com.production.noteflow.di

import android.content.Context
import androidx.room.Room
import com.production.noteflow.data.local.AppDatabase
import com.production.noteflow.data.local.NoteDao
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
        ).build()
    }

    @Provides
    fun provideNoteDao(
        database: AppDatabase
    ): NoteDao = database.noteDao()
}