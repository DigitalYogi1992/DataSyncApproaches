package com.digitalyogi.datasyncapproaches.di


import android.content.Context
import androidx.room.Room
import com.digitalyogi.datasyncapproaches.data.local.AppDatabase
import com.digitalyogi.datasyncapproaches.data.local.preferences.DataStoreManager
import com.digitalyogi.datasyncapproaches.data.local.preferences.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMyDao(db: AppDatabase) = db.myDao()

    @Provides
    @Singleton
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager {
        return SharedPrefManager(context)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}
