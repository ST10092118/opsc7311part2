package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.TimesheetEntriesAdapter
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.viewmodel.GoalViewModel
import com.opsc7311.opsc7311poepart2.viewmodel.TimesheetViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PomodoroTimesheetFragment : Fragment() {
    private val timesheetViewModel: TimesheetViewModel by viewModels()

    private lateinit var timesheetEntriesAdapter: TimesheetEntriesAdapter

    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pomodoro_timesheet, container, false)

        recyclerView = view.findViewById(R.id.timesheet_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        timesheetEntriesAdapter = TimesheetEntriesAdapter { timesheet, category ->
            redirectToPomodoro(timesheet)
        }

        recyclerView.adapter = timesheetEntriesAdapter

        getTimesheet()

        return view
    }

    private fun redirectToPomodoro(timesheet: Timesheet) {
        val bundle = Bundle().apply {
            putString("timesheetName", timesheet.name)
            putString("description", timesheet.description)
            putString("date", SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timesheet.date))
            putLong("startTime", timesheet.startTime)
            putLong("endTime", timesheet.endTime)
            putString("image", timesheet.image)
        }
        val pomodoroFragment = PomodoroFragment().apply {
            arguments = bundle
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, pomodoroFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getTimesheet() {
        timesheetViewModel._timesheets.observe(viewLifecycleOwner) { timesheet ->

            timesheetEntriesAdapter.updateData(timesheet)
        }
        timesheetViewModel.getTimesheetEntries()
    }

    private fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }
}