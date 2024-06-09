package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.CalenderAdapter
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.viewmodel.TimesheetViewModel
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarFragment : Fragment() {
    private val timesheetViewModel: TimesheetViewModel by viewModels()
    private lateinit var calAdapter: CalenderAdapter
    private lateinit var timesheets: ArrayList<Pair<Timesheet, Category>>

    private lateinit var filteredTimesheets: ArrayList<Pair<Timesheet, Category>>

    private lateinit var collapsibleCalendar: CollapsibleCalendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        collapsibleCalendar = view.findViewById(R.id.collapsibleCalendar)

        //val timetableLayout = view.findViewById<LinearLayout>(R.id.timetableLayout)
        val tasksRecyclerView = view.findViewById<RecyclerView>(R.id.timesheet_recycle_view)

//        for (hour in 0..23) {
//            val timeSlot = TextView(requireContext()).apply {
//                text = String.format("%02d:00", hour)
//                textSize = 16f
//                setPadding(8, 16, 8, 16)
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//            }
//            timetableLayout.addView(timeSlot)
//        }

        timesheets = arrayListOf<Pair<Timesheet, Category>>()
        filteredTimesheets = arrayListOf<Pair<Timesheet, Category>>()

        // Set up RecyclerView
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())



        calAdapter = CalenderAdapter{
                timesheet, category ->
            redirectToDetails(timesheet, category)
        }
        tasksRecyclerView.adapter = calAdapter

        getTimesheetEntries()

        handleSelectedDate()

        return view
    }
    private fun getTimesheetEntries(){
        timesheetViewModel._timesheets.observe(viewLifecycleOwner){
            // This method was adapted from stackoverflow
            // https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
            // CommonsWare
            // https://stackoverflow.com/users/115145/commonsware

                timesheetEntries ->
            calAdapter.updateData(timesheetEntries)
            setTimesheetEntries(timesheetEntries)
        }

        timesheetViewModel.getTimesheetEntries()

    }

    private fun redirectToDetails(timesheet: Timesheet, category: Category) {
        // This function was adapted from stackoverflow
        // https://stackoverflow.com/questions/46551228/how-to-pass-and-get-value-from-fragment-and-activity
        // Ankit Kumar
        // https://stackoverflow.com/users/3282461/ankit-kumar
        val bundle = Bundle().apply {
            putString("category", category.name)
            putString("color", category.color)
            putString("timesheetName", timesheet.name)
            putString("description", timesheet.description)
            putString("date", SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timesheet.date))
            putString("startTime", formatTime(timesheet.startTime))
            putString("endTime", formatTime(timesheet.endTime))
            putString("image", timesheet.image)
            // Add other data as needed
        }
        // Navigate to CategoryDetailsFragment
        val detailsFragment = TimesheetDetailsFragment().apply {
            arguments = bundle
        }

// Navigate to CategoryDetailsFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun filter(date: Date) {

        if (date.toString().isNotEmpty()) {
            filteredTimesheets.clear()


            for ((timesheet, category) in timesheets) {
                if (isSameDate(timesheet.date, date)) {
                    val timesheetData = Pair(timesheet, category)
                    filteredTimesheets.add(timesheetData)
                }
            }
            calAdapter.updateData(filteredTimesheets)
        }
    }

    private fun handleSelectedDate(){
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDayChanged() {}

            override fun onClickListener() {}

            override fun onDaySelect() {
                val selectedDay = collapsibleCalendar.selectedDay
                val selectedDate = Calendar.getInstance().apply {
                    if (selectedDay != null) {
                        set(selectedDay.year, selectedDay.month, selectedDay.day)
                    }
                }.time

                // Fetch and display tasks for the selected date
                filter(selectedDate)
            }

            override fun onItemClick(v: View) {}

            override fun onDataUpdate() {}

            override fun onMonthChange() {}

            override fun onWeekChange(i: Int) {}
        })
    }


    private fun isSameDate(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1

        val cal2 = Calendar.getInstance()
        cal2.time = date2

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }


    private fun setTimesheetEntries(timesheetEntries: List<Pair<Timesheet, Category>>){
        timesheets.clear()
        timesheets.addAll(timesheetEntries)
    }
    fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }
}