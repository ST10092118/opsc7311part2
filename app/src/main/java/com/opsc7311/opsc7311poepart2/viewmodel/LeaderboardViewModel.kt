package com.opsc7311.opsc7311poepart2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.database.service.LeaderboardService
import com.opsc7311.opsc7311poepart2.database.service.TimesheetService
import com.opsc7311.opsc7311poepart2.database.service.UserService

class LeaderboardViewModel: ViewModel() {
    private val timesheetService = TimesheetService()
    private val userService = UserService()
    val leaderboardService = LeaderboardService(timesheetService, userService)

    val error: MutableLiveData<String>  = MutableLiveData()
    val _leaderboard: MutableLiveData<List<Pair<User, Long>>> = MutableLiveData()

    fun fetchLeaderboard(){
        leaderboardService.fetchLeaderboard{
            leaderboard, message ->
            _leaderboard.postValue(leaderboard)

            if(message.isNotEmpty()){
                error.postValue(message)
            }
        }
    }
}