package com.digitalyogi.datasyncapproaches.data.repository

import com.digitalyogi.datasyncapproaches.data.local.MyDao
import com.digitalyogi.datasyncapproaches.data.local.MyEntity
import com.digitalyogi.datasyncapproaches.data.remote.FetchResult
import com.digitalyogi.datasyncapproaches.data.remote.RemoteDataSource
import com.digitalyogi.datasyncapproaches.domain.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstRepository @Inject constructor(
    private val myDao: MyDao,
    private val remoteDataSource: RemoteDataSource
) {

    // 1) Store data locally first (no network call)
    suspend fun addDataLocally(content: String) {
        withContext(Dispatchers.IO) {
            myDao.insertEntity(MyEntity(content = content, isSynced = false))
        }
    }

    // 2) Sync unsynced data in background
    suspend fun syncUnsyncedData(): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            val unsynced = myDao.getUnsyncedEntities()
            if (unsynced.isEmpty()) {
                return@withContext NetworkResult.Success(Unit)
            }

            for (entity in unsynced) {
                val result = remoteDataSource.sendDataSafely(entity.content)
                when (result) {
                    is FetchResult.Success -> {
                        // Mark as synced
                        myDao.updateEntity(entity.copy(isSynced = true))
                    }
                    is FetchResult.HttpError -> {
                        // Return up to ViewModel
                        return@withContext NetworkResult.HttpError(result.code, result.message)
                    }
                    is FetchResult.NetworkError -> {
                        return@withContext NetworkResult.NetworkError(result.exception)
                    }
                }
            }
            // If all unsynced items succeeded
            NetworkResult.Success(Unit)
        }
    }

    // 3) Get local data
    suspend fun getLocalData(): List<MyEntity> {
        return withContext(Dispatchers.IO) {
            myDao.getAllEntities()
        }
    }
}
