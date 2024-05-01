package com.opsc7311.opsc7311poepart2.database.model

import java.util.Date


data class Timesheet(
    val id: String = "",
    val name: String = "",
    val date: Date = Date(),
    val startTime: Long = 0,
    val endTime: Long = 0,
    val categoryId: String = "",
    val userId: String = ""
){
    constructor() : this("", "", Date(), 0, 0,"", "")
}
