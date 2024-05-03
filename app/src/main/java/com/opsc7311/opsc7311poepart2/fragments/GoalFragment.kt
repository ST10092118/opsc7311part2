package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.GoalViewModel


class GoalFragment : Fragment() {

    private val viewModel: GoalViewModel by viewModels()

    private lateinit var minimumText: TextView
    private lateinit var maximumText: TextView

    private lateinit var minimumInput: EditText
    private lateinit var maximumInput: EditText

    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_goal, container, false)

        minimumText = view.findViewById(R.id.minimumText)
        maximumText = view.findViewById(R.id.maximumText)

        minimumInput = view.findViewById(R.id.minimumInput)
        maximumInput = view.findViewById(R.id.maximumInput)

        submitButton = view.findViewById(R.id.save_button)

        getGoal()

        submitButton.setOnClickListener {
            setGoal()
            getGoal()
        }

        return view
    }
    private fun setGoal(){
        val minHrs = minimumInput.text.toString()
        val maxHrs = maximumInput.text.toString()

        if(minHrs.isEmpty()){
            Toast.makeText(requireContext(), "Enter a value for minimum hours",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(maxHrs.isEmpty()){
            Toast.makeText(requireContext(), "Enter a value for maximum hours",
                Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.status.observe(viewLifecycleOwner){
            // This method was adapted from stackoverflow
            // https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
            // CommonsWare
            // https://stackoverflow.com/users/115145/commonsware

            status ->
            if (status.first == RegistrationStatus.SUCCESS) {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();            }
        }

        viewModel.createNewGoal(minHrs.toInt(), maxHrs.toInt())
    }
    private fun getGoal(){
        var minHrs = 0
        var maxHrs = 0
        viewModel._goal.observe(viewLifecycleOwner){
            goal ->
            if(goal != null){
                minHrs = goal.minimumTime
                maxHrs = goal.maximumTime
            }
            minimumText.text = "${minHrs} hours"
            maximumText.text = "${maxHrs} hours"
        }
        viewModel.getGoal()
    }
}