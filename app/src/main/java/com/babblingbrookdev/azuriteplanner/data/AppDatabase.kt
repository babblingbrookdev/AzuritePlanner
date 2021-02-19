package com.babblingbrookdev.azuriteplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.babblingbrookdev.azuriteplanner.model.Entry

@Database(entities = [Entry::class], version =1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
}