package com.babblingbrookdev.azuriteplanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true)
    var entryId: Long = 0,
    var entryDate: Calendar = Calendar.getInstance(),
    var currentAzurite: Int,
    var azuriteGoal: Int,
    var baseProduction: Double,
    var rcAmount: Int
)
