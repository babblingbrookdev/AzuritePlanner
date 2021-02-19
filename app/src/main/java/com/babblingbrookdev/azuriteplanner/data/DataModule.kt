package com.babblingbrookdev.azuriteplanner.data

import androidx.room.Room
import com.babblingbrookdev.azuriteplanner.AzuritePlannerApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideDatabase(application: AzuritePlannerApp): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "AzuritePlanner.db"
        ).build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideEntryDao(appDatabase: AppDatabase): EntryDao {
        return appDatabase.entryDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideRepository(entryDao: EntryDao): Repository {
        return Repository(entryDao)
    }
}