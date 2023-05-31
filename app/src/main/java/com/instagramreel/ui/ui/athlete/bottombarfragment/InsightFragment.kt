package com.instagramreel.ui.ui.athlete.bottombarfragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

import com.instagramreel.databinding.FragmentInsightBinding
import com.instagramreel.ui.callbacks.onBottomNavigationClickAthlete

class InsightFragment : Fragment() {

    lateinit var binding: FragmentInsightBinding
    var callback: onBottomNavigationClickAthlete? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        groupBarChart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as onBottomNavigationClickAthlete
    }

    private fun groupBarChart() {
//        mChart = findViewById<View>(R.id.bar_chart) as BarChart
        binding.barChart.setDrawBarShadow(false)
        binding.barChart.description.isEnabled = false
        binding.barChart.setPinchZoom(false)
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.legend.isEnabled = false
        binding.barChart.axisLeft.setDrawLabels(false)

        // empty labels so that the names are spread evenly
        val labels = arrayOf(
            "",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec",
            ""
        )
        val xAxis: XAxis = binding.barChart.xAxis
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.textColor = Color.BLACK
        xAxis.textSize = 12f
        xAxis.axisLineColor = Color.WHITE
        xAxis.axisMinimum = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        val leftAxis: YAxis = binding.barChart.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.textSize = 12f
        leftAxis.axisLineColor = Color.WHITE
        leftAxis.setDrawGridLines(true)
        leftAxis.granularity = 2f
        leftAxis.setLabelCount(8, true)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        binding.barChart.axisRight.isEnabled = false
        binding.barChart.legend.isEnabled = false
        val valOne = floatArrayOf(10f, 20f, 30f, 40f, 50f,10f, 20f, 30f, 40f, 50f, 40f, 50f)
        val valTwo = floatArrayOf(60f, 50f, 40f, 30f, 20f,60f, 50f, 40f, 30f, 20f, 30f, 20f)
        val barOne: ArrayList<BarEntry> = ArrayList()
        val barTwo: ArrayList<BarEntry> = ArrayList()
        for (i in valOne.indices) {
            barOne.add(BarEntry(i.toFloat(), valOne[i]))
            barTwo.add(BarEntry(i.toFloat(), valTwo[i]))
        }
        val set1 = BarDataSet(barOne, "barOne")
        set1.color = Color.BLUE
        val set2 = BarDataSet(barTwo, "barTwo")
        set2.color = Color.RED
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        val data = BarData(dataSets)
        val groupSpace = 0.24f
        val barSpace = 0.08f
        val barWidth = 0.3f
        xAxis.setCenterAxisLabels(true)
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.barWidth = barWidth
        // so that the entire chart is shown when scrolled from right to left
        xAxis.axisMaximum = labels.size - 1.1f
        binding.barChart.data = data
        binding.barChart.setScaleEnabled(false)
        binding.barChart.isDoubleTapToZoomEnabled = false
        binding.barChart.setVisibleXRangeMaximum(6f)
        xAxis.setDrawGridLines(false)
        leftAxis.setDrawGridLines(false)
        binding.barChart.groupBars(1f, groupSpace, barSpace)
        binding.barChart.invalidate()
    }
}