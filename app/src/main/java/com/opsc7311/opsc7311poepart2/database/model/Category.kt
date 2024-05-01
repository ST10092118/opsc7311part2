package com.opsc7311.opsc7311poepart2.database.model




data class Category(
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val userId: String = ""
)
{
    constructor() : this("", "", "", "")
}
