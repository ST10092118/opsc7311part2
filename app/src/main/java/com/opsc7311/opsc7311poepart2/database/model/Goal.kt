package com.opsc7311.opsc7311poepart2.database.model




data class Goal(
    // This class was adapted from medium
    // https://medium.com/a-practical-guide-to-firebase-on-android/storing-and-retrieving-data-from-firebase-with-kotlin-on-android-91c36680771
    // Nick Skelton
    // https://medium.com/@nickskelton
    var id: String = "",
    val minimumTime: Int = 0,
    val maximumTime: Int = 0,
    val userId: String = ""
    ){
    constructor() : this("", 0, 0, "")
}
