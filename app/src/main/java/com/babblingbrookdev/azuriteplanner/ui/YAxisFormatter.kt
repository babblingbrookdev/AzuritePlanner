package com.babblingbrookdev.azuriteplanner.ui

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.math.ceil

class YAxisFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String = (ceil(value / 1000).toInt()).toString() + "K"
}