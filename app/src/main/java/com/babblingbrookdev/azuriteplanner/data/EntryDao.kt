package com.babblingbrookdev.azuriteplanner.data

import androidx.room.*
import com.babblingbrookdev.azuriteplanner.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Insert
    suspend fun insertEntry(entry: Entry): Long

    @Query("DELETE FROM Entry WHERE entryId = :entryId")
    suspend fun deleteEntryById(entryId: Long)

    @Update
    suspend fun updateEntry(entry: Entry)

    @Transaction
    @Query("SELECT * From Entry")
    fun getEntriesFlow(): Flow<List<Entry>>

    @Query("SELECT * FROM Entry WHERE entryId = :entryId")
    suspend fun getEntryById(entryId: Long): Entry?
}