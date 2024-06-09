package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.SpinnerAdapter
import com.opsc7311.opsc7311poepart2.adapters.TimesheetEntriesAdapter
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.viewmodel.GoalViewModel
import com.opsc7311.opsc7311poepart2.viewmodel.TimesheetViewModel
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class ReportsFragment : Fragment() {
    private val viewModel: GoalViewModel by viewModels()
    private val timesheetViewModel: TimesheetViewModel by viewModels()

    private var dailyGoalMin = 0
    private var dailyGoalMax = 0

    private lateinit var chart: BarChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var timesheetEntriesAdapter: TimesheetEntriesAdapter
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: SpinnerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        val spinnerArray = resources.getStringArray(R.array.spinner)

        chart = view.findViewById(R.id.barChart)
        spinner = view.findViewById(R.id.graph_spinner)

        recyclerView = view.findViewById(R.id.timesheet_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        timesheetEntriesAdapter = TimesheetEntriesAdapter { timesheet, category ->
            redirectToDetails(timesheet, category)
        }

        recyclerView.adapter = timesheetEntriesAdapter

        getGoal()
        getTimesheet()

        spinnerAdapter = SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                generateGraph(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return view
    }

    private fun generateGraph(selectedItem: String) {
        when (selectedItem) {
            "Week" -> {
                getTimesheetDataForGraph("week")
            }
            "Month" -> {
                getTimesheetDataForGraph("month")
            }
        }
    }

    private fun getTimesheetDataForGraph(timePeriod: String) {
        timesheetViewModel._timesheets.observe(viewLifecycleOwner) { timesheet ->
            val data = when (timePeriod) {
                "week" -> getWeeklyTasks(timesheet.map { it.first })
                "month" -> getMonthlyTasks(timesheet.map { it.first })
                else -> emptyMap()
            }
            setUpGraph(data, timePeriod)
        }
        timesheetViewModel.getTimesheetEntries()
    }

    private fun setUpGraph(data: Map<String, Int>, timePeriod: String) {
        val labels = if (timePeriod == "week") getPast7Days() else getMonthsOfYear()
        val newData = mutableMapOf<String, Int>()
        val entries = ArrayList<BarEntry>()
        var index = 0f
        var maxValue = 0

        for (label in labels) {
            val hour = data[label] ?: 0
            newData[label] = hour
        }

        for ((label, hours) in newData) {
            entries.add(BarEntry(index, hours.toFloat()))
            if (hours > maxValue) {
                maxValue = hours
            }
            index++
        }

        val dataSet = BarDataSet(entries, if (timePeriod == "week") "Weekly Hours" else "Monthly Hours")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.primary)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.blue)

        val barData = BarData(dataSet)
        chart.data = barData

        chart.setFitBars(true)
        chart.description.isEnabled = false
        chart.animateY(1000)
        chart.invalidate()

        val yAxis = chart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = if (dailyGoalMax > maxValue) dailyGoalMax + 10f else maxValue + 10f
        yAxis.granularity = 5f

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labels.size // Ensure all labels are shown

        chart.axisLeft.removeAllLimitLines()
        chart.axisLeft.addLimitLine(createLimitLine(dailyGoalMin, "Min - ${dailyGoalMin}hrs", R.color.blue))
        chart.axisLeft.addLimitLine(createLimitLine(dailyGoalMax, "Max - ${dailyGoalMax}hrs", R.color.green))

        // Add description with min and max hours
        val description = "Min Hours: $dailyGoalMin hrs\nMax Hours: $dailyGoalMax hrs"
        chart.description.text = description


        chart.axisRight.isEnabled = false

        // Create legend
        val legend = chart.legend
        val primaryColorLabel = if (timePeriod == "week") "Weekly Hours" else "Monthly Hours"
        val legendEntries = ArrayList<LegendEntry>()

        // Add primary color legend entry
        val primaryColorEntry = LegendEntry()
        primaryColorEntry.label = primaryColorLabel
        primaryColorEntry.formColor = ContextCompat.getColor(requireContext(), R.color.primary)
        primaryColorEntry.form = Legend.LegendForm.SQUARE
        legendEntries.add(primaryColorEntry)

        // Add minimum and maximum hours legend entries
        val minHoursEntry = LegendEntry()
        minHoursEntry.label = "Minimum hours"
        minHoursEntry.formColor = ContextCompat.getColor(requireContext(), R.color.blue)
        minHoursEntry.form = Legend.LegendForm.SQUARE
        legendEntries.add(minHoursEntry)

        val maxHoursEntry = LegendEntry()
        maxHoursEntry.label = "Maximum hours"
        maxHoursEntry.formColor = ContextCompat.getColor(requireContext(), R.color.green)
        maxHoursEntry.form = Legend.LegendForm.SQUARE
        legendEntries.add(maxHoursEntry)

        // Set custom legend entries
        legend.setCustom(legendEntries.toTypedArray())

        val minLimitLine = chart.axisLeft.limitLines[0] // Assuming it's the first limit line
        minLimitLine.enableDashedLine(10f, 10f, 0f) // Customize the dashed line
        minLimitLine.lineColor = ContextCompat.getColor(requireContext(), R.color.blue)

        val maxLimitLine = chart.axisLeft.limitLines[1] // Assuming it's the second limit line
        maxLimitLine.enableDashedLine(10f, 10f, 0f) // Customize the dashed line
        maxLimitLine.lineColor = ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun createLimitLine(value: Int, label: String, colorResId: Int): LimitLine {
        val limitLine = LimitLine(value.toFloat(), label)
        limitLine.lineWidth = 2f
        limitLine.enableDashedLine(10f, 10f, 0f)
        limitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        limitLine.textColor = ContextCompat.getColor(requireContext(), colorResId)
        return limitLine
    }

    private fun getGoal() {
        viewModel._goal.observe(viewLifecycleOwner) { goal ->
            if (goal != null) {
                dailyGoalMin = goal.minimumTime
                dailyGoalMax = goal.maximumTime
            }
        }
        viewModel.getGoal()
    }

    private fun getTimesheet() {
        timesheetViewModel._timesheets.observe(viewLifecycleOwner) { timesheet ->
            val data = getWeeklyTasks(timesheet.map { it.first })
            setUpGraph(data, "week")
            timesheetEntriesAdapter.updateData(timesheet)
        }
        timesheetViewModel.getTimesheetEntries()
    }

    private fun getWeeklyTasks(timesheetEntries: List<Timesheet>): Map<String, Int> {
        val weeklyData = mutableMapOf<String, Int>()
        val tasksByWeek = timesheetEntries.groupBy { timesheet ->
            val calendar = Calendar.getInstance()
            calendar.time = timesheet.date
            calendar[Calendar.DAY_OF_WEEK]
        }

        for ((dayOfWeek, tasksInDay) in tasksByWeek) {
            val totalHours = tasksInDay.sumBy { (it.endTime - it.startTime).toInt() } / (1000 * 60 * 60)
            val dayName = DateFormatSymbols().shortWeekdays[dayOfWeek]
            weeklyData[dayName] = totalHours
        }

        return weeklyData
    }

    private fun getMonthlyTasks(timesheetEntries: List<Timesheet>): Map<String, Int> {
        val monthlyData = mutableMapOf<String, Int>()
        val tasksByMonth = timesheetEntries.groupBy { timesheet ->
            val calendar = Calendar.getInstance()
            calendar.time = timesheet.date
            calendar[Calendar.MONTH]
        }

        for ((month, tasksInMonth) in tasksByMonth) {
            val totalHours = tasksInMonth.sumBy { (it.endTime - it.startTime).toInt() } / (1000 * 60 * 60)
            val monthName = DateFormatSymbols().shortMonths[month]
            monthlyData[monthName] = totalHours
        }

        return monthlyData
    }

    private fun getPast7Days(): List<String> {
        val calendar = Calendar.getInstance()
        val daysOfWeek = mutableListOf<String>()

        for (i in 0..6) {
            val dayName = DateFormatSymbols().shortWeekdays[calendar[Calendar.DAY_OF_WEEK]]
            daysOfWeek.add(dayName)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        return daysOfWeek.reversed()
    }

    private fun getMonthsOfYear(): List<String> {
        return DateFormatSymbols().shortMonths.toList()
    }

    private fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }

    private fun redirectToDetails(timesheet: Timesheet, category: Category) {
        val bundle = Bundle().apply {
            putString("category", category.name)
            putString("color", category.color)
            putString("timesheetName", timesheet.name)
            putString("description", timesheet.description)
            putString("date", SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timesheet.date))
            putString("startTime", formatTime(timesheet.startTime))
            putString("endTime", formatTime(timesheet.endTime))
            putString("image", timesheet.image)
        }
        val detailsFragment = TimesheetDetailsFragment().apply {
            arguments = bundle
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, detailsFragment)
            .addToBackStack(null)
            .commit()
    }
}
