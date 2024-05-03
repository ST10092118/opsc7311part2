package com.opsc7311.opsc7311poepart2.database.model

import java.util.Date


data class Timesheet(
    // This class was adapted from medium
    // https://medium.com/a-practical-guide-to-firebase-on-android/storing-and-retrieving-data-from-firebase-with-kotlin-on-android-91c36680771
    // Nick Skelton
    // https://medium.com/@nickskelton
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val date: Date = Date(),
    val startTime: Long = 0,
    val endTime: Long = 0,
    val image: String = "",
    val categoryId: String = "",
    val userId: String = ""
){
    constructor() : this("", "", "", Date(), 0, 0,"", "", "")
}
