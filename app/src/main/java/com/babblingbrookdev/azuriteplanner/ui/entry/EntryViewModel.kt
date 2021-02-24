package com.babblingbrookdev.azuriteplanner.ui.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babblingbrookdev.azuriteplanner.data.Repository
import com.babblingbrookdev.azuriteplanner.model.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val newEntryId: LiveData<Long> get() = _newEntryId
    private val _newEntryId = MutableLiveData<Long>()

    val editEntry: LiveData<Entry> get() = _editEntry
    private val _editEntry = MutableLiveData<Entry>()

    fun addEntry(current: Int, goal: Int, production: Double, rcAmount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entry = Entry(
                entryDate = Calendar.getInstance(),
                currentAzurite = current,
                azuriteGoal = goal,
                baseProduction = production,
                rcAmount = rcAmount
            )
            val entryId = repository.insertEntry(entry)
            withContext(Dispatchers.Main) { _newEntryId.value = entryId }
        }
    }

    fun editEntry(editEntry: Entry, current: Int, goal: Int, production: Double, rcAmount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entry = repository.getEntryById(editEntry.entryId)
            entry?.let {
                it.currentAzurite = current
                it.azuriteGoal = goal
                it.baseProduction = production
                it.rcAmount = rcAmount
                repository.updateEntry(entry)
            }
        }
    }

    fun getEntryById(entryId: Long) {
        viewModelScope.launch {
            _editEntry.value = repository.getEntryById(entryId)
        }
    }
}