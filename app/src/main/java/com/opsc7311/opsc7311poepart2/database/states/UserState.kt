package com.opsc7311.opsc7311poepart2.database.states

data class UserState(

    val username: String = "",
    val password: String = "",
    val isAddingUser: Boolean = false
)
