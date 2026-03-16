package com.production.noteflow.di

import com.production.noteflow.data.repository.NoteRepositoryImpl
import com.production.noteflow.data.repository.QuoteRepositoryImpl
import com.production.noteflow.data.repository.ReminderRepositoryImpl
import com.production.noteflow.domain.repository.NoteRepository
import com.production.noteflow.domain.repository.QuoteRepository
import com.production.noteflow.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
        impl: ReminderRepositoryImpl
    ): ReminderRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(
        impl: QuoteRepositoryImpl
    ): QuoteRepository
}

//import com.production.noteflow.data.local.room.daos.NoteDao
//import com.production.noteflow.data.local.room.daos.ReminderDao
//import com.production.noteflow.data.repository.NoteRepository
//import com.production.noteflow.data.repository.ReminderRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @Singleton
//    fun provideNoteRepository(
//        noteDao: NoteDao
//    ): NoteRepository {
//        return NoteRepository(noteDao)
//    }
//
//    @Provides
//    @Singleton
//    fun provideReminderRepository(reminderDao: ReminderDao): ReminderRepository {
//        return ReminderRepository(reminderDao)
//    }
//}