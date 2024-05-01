package com.opsc7311.opsc7311poepart2.database.model




data class User(
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = ""
) {
    constructor() : this("", "", "", "")
}