package com.production.noteflow.services.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.production.noteflow.data.repository.QuoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyQuoteWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val quoteRepository: QuoteRepository
) : CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return quoteRepository.refreshQuote(force = true)
            .fold(
                onSuccess = {
                    QuoteWorkScheduler.scheduleNext(applicationContext)
                    Result.success()
                },
                onFailure = {
                    Result.retry()
                }
            )
    }
}