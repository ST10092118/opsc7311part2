package com.opsc7311.opsc7311poepart2.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.TimesheetEntriesAdapter
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.viewmodel.CategoryViewModel
import com.opsc7311.opsc7311poepart2.viewmodel.TimesheetViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EntriesFragment : Fragment() {

    private val timesheetViewModel: TimesheetViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var navigateToAddCategory: FrameLayout

    private lateinit var timesheetEntriesAdapter: TimesheetEntriesAdapter

    //filter_date

    private lateinit var filterDate: TextView
    private lateinit var filterButton: FrameLayout


    private lateinit var timesheets: ArrayList<Pair<Timesheet, Category>>

    private lateinit var filteredTimesheets: ArrayList<Pair<Timesheet, Category>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        filterDate = view.findViewById(R.id.filter_date)
        filterButton = view.findViewById(R.id.filter_button)


        navigateToAddCategory = view.findViewById(R.id.to_add_timesheet)

        recyclerView = view.findViewById(R.id.timesheet_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)


        timesheets = arrayListOf<Pair<Timesheet, Category>>()
        filteredTimesheets = arrayListOf<Pair<Timesheet, Category>>()

        timesheetEntriesAdapter = TimesheetEntriesAdapter{
                timesheet, category ->
            redirectToDetails(timesheet, category)
        }

        recyclerView.adapter = timesheetEntriesAdapter


        navigateToAddCategory.setOnClickListener{
            navigateToCreate()
        }

        filterButton.setOnClickListener{
            showDatePickerDialog()
        }

        getTimesheetEntries()

        return view
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

    fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }

    private fun navigateToCreate() {
        val fragment = TimesheetEntryFragment()

        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }
    private fun getTimesheetEntries(){
        timesheetViewModel._timesheets.observe(viewLifecycleOwner){
            // This method was adapted from stackoverflow
            // https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
            // CommonsWare
            // https://stackoverflow.com/users/115145/commonsware

            timesheetEntries ->
            timesheetEntriesAdapter.updateData(timesheetEntries)
            setTimesheetEntries(timesheetEntries)
        }

        timesheetViewModel.getTimesheetEntries()
    }

    private fun showDatePickerDialog() {


        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                    // Update the TextView with the selected date
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    filterDate.text = selectedDate
                    filter()
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun filter() {
        val date = filterDate.text.toString()

        if (date.isNotEmpty()) {
            filteredTimesheets.clear()

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)

            for ((timesheet, category) in timesheets) {
                if (isSameDate(timesheet.date, parsedDate)) {
                    val timesheetData = Pair(timesheet, category)
                    filteredTimesheets.add(timesheetData)
                }
            }
            timesheetEntriesAdapter.updateData(filteredTimesheets)
        }
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
}