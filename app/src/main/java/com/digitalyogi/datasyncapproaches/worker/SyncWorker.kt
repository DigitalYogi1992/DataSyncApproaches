package com.digitalyogi.datasyncapproaches.worker


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.digitalyogi.datasyncapproaches.data.repository.OfflineFirstRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val offlineFirstRepository: OfflineFirstRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Attempt to sync unsynced data
            offlineFirstRepository.syncUnsyncedData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
