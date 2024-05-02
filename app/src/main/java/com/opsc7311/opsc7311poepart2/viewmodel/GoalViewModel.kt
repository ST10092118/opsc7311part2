package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Goal
import com.opsc7311.opsc7311poepart2.database.service.CategoryService
import com.opsc7311.opsc7311poepart2.database.service.GoalService
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class GoalViewModel: ViewModel() {
    val goalService = GoalService()
    val _goal = MutableLiveData<Goal?>()
    val status: MutableLiveData<Pair<RegistrationStatus, String>> = MutableLiveData()


    fun createNewGoal(minimum: Int, maximum: Int){
        goalService.createNewGoal(minimum, maximum) { success, message ->
            status.postValue(Pair(success, message))
        }
    }

    fun getGoal() {
        goalService.getGoal { goal ->
            if (goal != null) {
                //registrationStatus.postValue(true)
                _goal.postValue(goal)
            } else {
                _goal.postValue(null)
                status.postValue(Pair(RegistrationStatus.FAILURE, "No categories."))
            }
        }
    }
}