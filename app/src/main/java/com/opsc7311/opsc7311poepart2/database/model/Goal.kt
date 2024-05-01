package com.opsc7311.opsc7311poepart2.database.model




data class Goal(
    var id: String = "",
    val minimumTime: Int = 0,
    val maximumTime: Int = 0,
    val userId: String = ""
    ){
    constructor() : this("", 0, 0, "")
}
