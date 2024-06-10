package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.TimeBar


class PomodoroFragment : Fragment() {

    private var startTimeInMillis: Long = 180000 // 30 minutes in milliseconds
    private var timeLeftInMillis: Long = startTimeInMillis
    private var timerRunning = false
    private var countDownTimer: CountDownTimer? = null

    private lateinit var timeBar : TimeBar

    private lateinit var startButton: Button
    private lateinit var resetButton: Button

    private lateinit var timesheetName: TextView
    private lateinit var intervalTextView: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pomodoro, container, false)

        timeBar = view.findViewById<TimeBar>(R.id.progress_bar)
        startButton = view.findViewById<Button>(R.id.start_button)
        timesheetName = view.findViewById(R.id.timesheet_name)
        resetButton = view.findViewById(R.id.reset_button)

        timesheetName.text = arguments?.getString("timesheetName")

        val duration = arguments?.getLong("endTime")?.minus(arguments?.getLong("startTime")!!)

        Log.d("Long", duration.toString())



        duration?.let { formatDuration(it) }?.let { timeBar.setTimerText(it) }

        startButton.setOnClickListener {
            if (duration != null) {
                timeBar.startTimer(duration)
            } // Start 25-minute timer
        }

        resetButton.setOnClickListener {
            timeBar.stopTimer()
        }


        return view
    }

    fun formatDuration(duration: Long): String {
        val totalSeconds = duration / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}