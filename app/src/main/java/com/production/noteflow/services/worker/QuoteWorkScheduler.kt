package com.production.noteflow.services.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object QuoteWorkScheduler {

    private const val UNIQUE_WORK_NAME = "daily_quote_midnight_work"

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleNext(context: Context) {
        val delay = calculateInitialDelayToNextMidnight()

        val request = OneTimeWorkRequestBuilder<DailyQuoteWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateInitialDelayToNextMidnight(): Long {
        val zoneId = ZoneId.systemDefault()
        val now = LocalDateTime.now(zoneId)
        val nextMidnight = LocalDate.now(zoneId)
            .plusDays(1)
            .atStartOfDay()

        return Duration.between(now, nextMidnight).toMillis()
    }
}