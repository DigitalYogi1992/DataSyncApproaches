package com.digitalyogi.datasyncapproaches.data.repository

import com.digitalyogi.datasyncapproaches.data.local.MyDao
import com.digitalyogi.datasyncapproaches.data.local.MyEntity
import com.digitalyogi.datasyncapproaches.data.remote.FetchResult
import com.digitalyogi.datasyncapproaches.data.remote.RemoteDataSource
import com.digitalyogi.datasyncapproaches.domain.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HybridRepository @Inject constructor(
    private val myDao: MyDao,
    private val remoteDataSource: RemoteDataSource
) {

    // Attempt immediate server push, fallback local if fails
    suspend fun addData(content: String): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            val result = remoteDataSource.sendDataSafely(content)
            when (result) {
                is FetchResult.Success -> {
                    // Mark as synced
                    myDao.insertEntity(MyEntity(content = content, isSynced = true))
                    NetworkResult.Success(Unit)
                }
                is FetchResult.HttpError -> {
                    // Store locally unsynced but return an error
                    myDao.insertEntity(MyEntity(content = content, isSynced = false))
                    NetworkResult.HttpError(result.code, result.message)
                }
                is FetchResult.NetworkError -> {
                    // If no network or unknown error, store unsynced
                    myDao.insertEntity(MyEntity(content = content, isSynced = false))
                    NetworkResult.NetworkError(result.exception)
                }
            }
        }
    }

    suspend fun syncPendingData(): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            val unsynced = myDao.getUnsyncedEntities()
            if (unsynced.isEmpty()) {
                return@withContext NetworkResult.Success(Unit)
            }
            for (entity in unsynced) {
                val result = remoteDataSource.sendDataSafely(entity.content)
                when (result) {
                    is FetchResult.Success -> {
                        myDao.updateEntity(entity.copy(isSynced = true))
                    }
                    is FetchResult.HttpError ->
                        return@withContext NetworkResult.HttpError(result.code, result.message)
                    is FetchResult.NetworkError ->
                        return@withContext NetworkResult.NetworkError(result.exception)
                }
            }
            NetworkResult.Success(Unit)
        }
    }

    suspend fun getLocalData(): List<MyEntity> {
        return withContext(Dispatchers.IO) {
            myDao.getAllEntities()
        }
    }
}
