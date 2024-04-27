package com.opsc7311.opsc7311poepart2.database.events

import java.util.Date

sealed interface  TimesheetEvent {
    object saveTimesheet: TimesheetEvent
    data class setName(val timesheetName: String): TimesheetEvent
    data class setDate(val date: Date): TimesheetEvent
    data class setStartTime(val startTime: Long): TimesheetEvent
    data class setEndTime(val endTime: Long) : TimesheetEvent
    data class setCategoryId(val categoryId: Int): TimesheetEvent
    data class setUserId(val UserId: Int): TimesheetEvent
    object showDialog: TimesheetEvent

    object hideObject: TimesheetEvent

}