package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.opsc7311.opsc7311poepart2.R
import com.bumptech.glide.Glide


class TimesheetDetailsFragment : Fragment() {


    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var category: TextView
    private lateinit var date: TextView
    private lateinit var startTime: TextView
    private lateinit var  endTextView: TextView
    private lateinit var image: ImageView

    private val colorMap = mapOf(
        "Red" to R.color.red,
        "Blue" to R.color.blue,
        "Green" to R.color.green,
        "Yellow" to R.color.yellow,
        "Orange" to R.color.primary,
        "Black" to R.color.black,
        "Grey" to R.color.grey,
        "Purple" to R.color.purple
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timesheet_details, container, false)

        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description_text)
        category = view.findViewById(R.id.category)
        date = view.findViewById(R.id.date)
        startTime = view.findViewById(R.id.start_time_textview)
        endTextView = view.findViewById(R.id.end_time_textview)
        image = view.findViewById(R.id.saved_image)

        val catName = arguments?.getString("category")
        val catColor = arguments?.getString("color")
        val timesheetName = arguments?.getString("timesheetName")
        val tDescription = arguments?.getString("description")
        val startTimeVal = arguments?.getString("startTime")
        val endTimeVal = arguments?.getString("endTime")
        val imageVal = arguments?.getString("image")
        val dateVal = arguments?.getString("date")

        category.text = catName
        name.text = timesheetName
        description.text = tDescription
        date.text = dateVal
        startTime.text = startTimeVal
        endTextView.text = endTimeVal

        val colorId = colorMap[catColor]

        val color = if (colorId != null) {
            ContextCompat.getColor(requireContext(), colorId)
        } else {
            ContextCompat.getColor(requireContext(), R.color.black)
        }

        category.setTextColor(color)


        Glide.with(this@TimesheetDetailsFragment)
            .load(imageVal)
            .into(image)

        return view
    }


}