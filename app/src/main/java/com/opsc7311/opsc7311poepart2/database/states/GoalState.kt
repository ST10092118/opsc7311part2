package com.opsc7311.opsc7311poepart2.database.states

data class GoalState(
    val minimumTime: Int = 0,
    val maximumTime: Int = 0,
    val userId: Int = 0,
    val isAddingGoal: Boolean = false

)
