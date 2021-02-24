package com.babblingbrookdev.azuriteplanner.data

import android.content.Context
import androidx.room.Room
import com.babblingbrookdev.azuriteplanner.AzuritePlannerApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext application: Context): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "AzuritePlanner.db"
        ).build()
    }

    @Provides
    fun provideEntryDao(appDatabase: AppDatabase): EntryDao {
        return appDatabase.entryDao()
    }

    @Singleton
    @Provides
    fun provideRepository(entryDao: EntryDao) = Repository(entryDao)
}