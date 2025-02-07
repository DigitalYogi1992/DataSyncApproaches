package com.digitalyogi.datasyncapproaches.data.repository

import com.digitalyogi.datasyncapproaches.data.local.MyDao
import com.digitalyogi.datasyncapproaches.data.local.MyEntity
import com.digitalyogi.datasyncapproaches.data.remote.FetchResult
import com.digitalyogi.datasyncapproaches.data.remote.RemoteDataSource
import com.digitalyogi.datasyncapproaches.domain.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteFirstRepository @Inject constructor(
    private val myDao: MyDao,
    private val remoteDataSource: RemoteDataSource
) {

    // 1) Send to server first
    suspend fun addDataRemote(content: String): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            val result = remoteDataSource.sendDataSafely(content)
            when (result) {
                is FetchResult.Success -> {
                    // Cache locally if server says success
                    myDao.insertEntity(MyEntity(content = content, isSynced = true))
                    NetworkResult.Success(Unit)
                }
                is FetchResult.HttpError ->
                    NetworkResult.HttpError(result.code, result.message)
                is FetchResult.NetworkError ->
                    NetworkResult.NetworkError(result.exception)
            }
        }
    }

    // 2) Fetch from server, then cache in local DB
    suspend fun fetchAndCacheData(): NetworkResult<List<MyEntity>> {
        return withContext(Dispatchers.IO) {
            val fetchResult = remoteDataSource.fetchDataSafely()
            when (fetchResult) {
                is FetchResult.Success -> {
                    val items = fetchResult.data.map { req ->
                        MyEntity(content = req.content, isSynced = true)
                    }
                    items.forEach { myDao.insertEntity(it) }
                    NetworkResult.Success(items)
                }
                is FetchResult.HttpError -> {
                    NetworkResult.HttpError(fetchResult.code, fetchResult.message)
                }
                is FetchResult.NetworkError -> {
                    NetworkResult.NetworkError(fetchResult.exception)
                }
            }
        }
    }

    // 3) Get local data
    suspend fun getLocalData(): List<MyEntity> {
        return withContext(Dispatchers.IO) {
            myDao.getAllEntities()
        }
    }
}
