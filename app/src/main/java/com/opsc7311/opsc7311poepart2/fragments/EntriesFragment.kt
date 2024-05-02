package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.viewmodel.CategoryViewModel
import com.opsc7311.opsc7311poepart2.viewmodel.TimesheetViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EntriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntriesFragment : Fragment() {

    private val timesheetViewModel: TimesheetViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var navigateToAddCategory: FrameLayout


    private lateinit var timesheets: ArrayList<Timesheet>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        navigateToAddCategory = view.findViewById(R.id.to_add_timesheet)

        recyclerView = view.findViewById(R.id.timesheet_recycle_view)


        timesheets = arrayListOf<Timesheet>()

        navigateToAddCategory.setOnClickListener{
            navigateToCreate()
        }

        return view
    }
    private fun navigateToCreate() {
        val fragment = TimesheetEntryFragment()

        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }

}