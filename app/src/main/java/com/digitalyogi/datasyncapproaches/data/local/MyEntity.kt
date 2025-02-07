package com.digitalyogi.datasyncapproaches.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_entity")
data class MyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val isSynced: Boolean = false
)
