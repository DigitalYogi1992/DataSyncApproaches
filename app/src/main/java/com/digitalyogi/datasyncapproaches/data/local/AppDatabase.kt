package com.digitalyogi.datasyncapproaches.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}
