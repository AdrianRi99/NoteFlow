package com.production.noteflow.di

import com.production.noteflow.domain.scheduler.ReminderScheduler
import com.production.noteflow.service.reminder.ReminderSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {

    @Binds
    @Singleton
    abstract fun bindReminderScheduler(
        impl: ReminderSchedulerImpl
    ): ReminderScheduler
}