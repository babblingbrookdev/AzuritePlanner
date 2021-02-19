package com.babblingbrookdev.azuriteplanner.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.babblingbrookdev.azuriteplanner.data.Repository
import com.babblingbrookdev.azuriteplanner.model.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getEntries(): LiveData<List<Entry>> {
        return repository.getEntriesFlow().asLiveData(Dispatchers.IO)
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            repository.deleteEntry(entry.entryId)
        }
    }
}