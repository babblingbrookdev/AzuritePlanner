package com.babblingbrookdev.azuriteplanner.ui.chart

import androidx.lifecycle.*
import com.babblingbrookdev.azuriteplanner.data.Repository
import com.babblingbrookdev.azuriteplanner.model.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getEntries(): LiveData<List<Entry>> {
        return repository.getEntriesFlow().asLiveData(Dispatchers.IO)
    }
}