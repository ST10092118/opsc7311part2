package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.Dao.GoalDao
import com.opsc7311.opsc7311poepart2.database.events.CategoryEvent
import com.opsc7311.opsc7311poepart2.database.events.GoalEvent
import com.opsc7311.opsc7311poepart2.database.states.CategoryState
import com.opsc7311.opsc7311poepart2.database.states.GoalState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class GoalViewModel(private val dao: GoalDao): ViewModel() {
    private val _state = MutableStateFlow(GoalState())

    fun onEvent(event: GoalEvent) {
        when(event){
            GoalEvent.hideObject -> _state.update {
                it.copy(
                    isAddingGoal = false
                )
            }
            GoalEvent.saveGoal -> {
            }
            is GoalEvent.setMaximumTime -> _state.update {
                it.copy(
                    maximumTime = event.setMaximumTime
                )
            }
            is GoalEvent.setMminimumTime -> _state.update {
                it.copy(
                    minimumTime = event.minimumTime
                )
            }
            is GoalEvent.setUserId -> _state.update {
                it.copy(
                    userId = event.UserId
                )
            }
            GoalEvent.showDialog -> _state.update {
                it.copy(
                    isAddingGoal = false
                )
            }
        }

    }
}