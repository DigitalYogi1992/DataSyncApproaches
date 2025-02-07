package com.digitalyogi.datasyncapproaches.worker


import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

object WorkManagerHelper {
    fun enqueueSync(context: Context) {
        val request = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("SyncWork", ExistingWorkPolicy.KEEP, request)
    }
}
