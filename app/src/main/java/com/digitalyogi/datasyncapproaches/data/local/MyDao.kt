package com.digitalyogi.datasyncapproaches.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: MyEntity): Long

    @Query("SELECT * FROM my_entity")
    suspend fun getAllEntities(): List<MyEntity>

    @Query("SELECT * FROM my_entity WHERE isSynced = 0")
    suspend fun getUnsyncedEntities(): List<MyEntity>

    @Update
    suspend fun updateEntity(entity: MyEntity)
}
