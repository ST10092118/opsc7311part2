package com.opsc7311.opsc7311poepart2.database.states

import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import java.util.Date

data class TimesheetState(
    val Timesheets: List<Timesheet> = emptyList(),
    val name: String = "",
    val date: Date = Date(),
    val startTime: Long = 0,
    val endTime: Long = 0,
    val categoryId: Int = 0,
    val userId: Int = 0,
    val isAddingTimesheet: Boolean = false
)
