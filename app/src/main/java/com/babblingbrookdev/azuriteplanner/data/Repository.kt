package com.babblingbrookdev.azuriteplanner.data

import com.babblingbrookdev.azuriteplanner.model.Entry
import javax.inject.Inject

class Repository @Inject constructor(private val entryDao: EntryDao) {

    suspend fun insertEntry(entry: Entry): Long {
        return entryDao.insertEntry(entry)
    }

    fun getEntriesFlow() = entryDao.getEntriesFlow()

    suspend fun getEntryById(entryId: Long) = entryDao.getEntryById(entryId)

    suspend fun updateEntry(entry: Entry) = entryDao.updateEntry(entry)

    suspend fun deleteEntry(entryId: Long) {
        entryDao.deleteEntryById(entryId)
    }
}