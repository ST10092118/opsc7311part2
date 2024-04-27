package com.opsc7311.opsc7311poepart2.database.events

sealed interface GoalEvent {
    object saveGoal: GoalEvent
    data class setMminimumTime(val minimumTime: Int): GoalEvent
    data class setMaximumTime(val setMaximumTime: Int): GoalEvent
    data class setUserId(val UserId: Int): GoalEvent
    object showDialog: GoalEvent

    object hideObject: GoalEvent


}