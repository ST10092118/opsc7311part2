package com.opsc7311.opsc7311poepart2.database.events

sealed interface UserEvent {
    object registerUser: UserEvent

    data class setUsername(val username: String): UserEvent

    data class setPassword(val password: String): UserEvent

    object showDialog: UserEvent

    object hideObject: UserEvent


}