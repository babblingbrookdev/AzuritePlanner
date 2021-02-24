package com.babblingbrookdev.azuriteplanner.ui.chart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.babblingbrookdev.azuriteplanner.R
import com.babblingbrookdev.azuriteplanner.databinding.FragmentChartBinding
import com.babblingbrookdev.azuriteplanner.ui.DateFormatter
import com.babblingbrookdev.azuriteplanner.ui.FragmentListener
import com.babblingbrookdev.azuriteplanner.ui.YAxisFormatter
import com.babblingbrookdev.azuriteplanner.ui.entry.EntryFragment
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException
import java.util.*
import kotlin.math.floor

@AndroidEntryPoint
class ChartFragment : Fragment() {

    private val viewModel: ChartViewModel by viewModels()
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private var _listener: FragmentListener? = null
    private val listener get() = _listener!!

    companion object {
        private val DAY_UNIT = 86400000L // in millis
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            _listener = context
        } else {
            throw RuntimeException("$context must implement ChartFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // show entry fragment on click of floating action button
        listener.getActivityFab().setOnClickListener {
            val entryFragment = EntryFragment()
            entryFragment.show(requireActivity().supportFragmentManager, entryFragment.tag)
        }

        // build chart when entries are received from database
        viewModel.getEntries().observe(viewLifecycleOwner, Observer {
            generateLineData(it)
        })
    }

    private fun generateLineData(values: List<com.babblingbrookdev.azuriteplanner.model.Entry>) {
        val caDataSet = ArrayList<Entry>() // current azurite data set
        val fpDataSet = ArrayList<Entry>() // future production data set

        values.forEach {
            caDataSet.add(Entry(it.entryDate.timeInMillis.toFloat(), it.currentAzurite.toFloat()))
        }

        // line data for current production
        val cpLineData = LineDataSet(caDataSet, "Current azurite")
        cpLineData.apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
            setDrawFilled(true)
            setDrawCircles(true)
            lineWidth = 1.8f
            highLightColor = Color.rgb(51, 116, 255)
            color = Color.rgb(51, 178, 255)
            fillColor = Color.rgb(51, 178, 255)
            fillAlpha = 100
        }

        binding.linechart.setNoDataTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        binding.linechart.invalidate()

        // if there is chart data, build a chart
        values.takeIf { values.isNotEmpty() }?.let {
            // calculate time remaining for header
            val durationUntilGoal = ((it.last().azuriteGoal - it.last().currentAzurite) /
                    ((it.last().baseProduction * 24) + it.last().rcAmount))
            val days = floor(durationUntilGoal).toInt()
            val hours = (24 * (durationUntilGoal - floor(durationUntilGoal))).toInt()
            binding.daysRemaining.text =
                String.format(resources.getString(R.string.duration_remaining), days, hours)

            val goal = values.last().azuriteGoal

            // calculate future production using last entry data
            var time = values.last().entryDate.timeInMillis
            var currentAzurite = values.last().currentAzurite.toDouble()
            while (currentAzurite < goal) {
                fpDataSet.add(Entry(time.toFloat(), currentAzurite.toFloat()))
                time += DAY_UNIT
                currentAzurite += (values.last().baseProduction * 24) + values.last().rcAmount
            }

            // line data for future production
            val fpLineData = LineDataSet(fpDataSet, "Future production")
            fpLineData.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.8f
                setDrawFilled(true)
                setDrawCircles(false)
                lineWidth = 1.8f
                setCircleColor(Color.rgb(255, 90, 40))
                highLightColor = Color.rgb(255, 87, 34)
                color = Color.rgb(255, 87, 15)
                fillColor = Color.rgb(255, 87, 15)
                fillAlpha = 100
            }

            binding.linechart.apply {
                // add goal line to chart
                val goalLine =
                    LimitLine(
                        goal.toFloat(), String.format(
                            resources.getString(R.string.azurite_goal_label),
                            goal
                        )
                    )
                goalLine.lineColor = Color.rgb(255, 87, 34)
                goalLine.lineWidth = 2f
                axisLeft.removeAllLimitLines()
                axisLeft.addLimitLine(goalLine)

                // add date labels to x-axis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = DateFormatter()
                xAxis.labelCount = 8

                // add "k" labels to azurite totals
                axisRight.isEnabled = false
                axisLeft.valueFormatter = YAxisFormatter()
                axisLeft.labelCount = 10
                axisLeft.axisMaximum = (it.last().azuriteGoal * 1.10f)
                axisLeft.axisMinimum = 0f

                // set chart preferences
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true
                setScaleEnabled(true)

                // apply datasets to chart
                val dataset = ArrayList<ILineDataSet>()
                dataset.add(cpLineData)
                dataset.add(fpLineData)
                val lineData = LineData(dataset)
                lineData.setValueTextSize(9f)
                lineData.setDrawValues(false)
                data = lineData
                notifyDataSetChanged()

                // set initial location of chart and viewport size
                setVisibleXRangeMaximum(DAY_UNIT.toFloat() * 8)
                setVisibleYRangeMinimum((goal * 1.10f), YAxis.AxisDependency.LEFT)
                moveViewToX((System.currentTimeMillis()).toFloat() - (DAY_UNIT * 5))
                invalidate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _listener = null
        _binding = null
    }
}