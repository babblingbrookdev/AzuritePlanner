package com.babblingbrookdev.azuriteplanner.ui

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val date = Date(value.toLong())
        val sdf = SimpleDateFormat("MM/dd", Locale.ENGLISH)

        return sdf.format(date)
    }
}