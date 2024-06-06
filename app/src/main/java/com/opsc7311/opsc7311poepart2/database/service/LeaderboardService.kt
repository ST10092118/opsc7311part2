package com.opsc7311.opsc7311poepart2.database.service

import android.util.Log
import com.opsc7311.opsc7311poepart2.database.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

class LeaderboardService(private val timesheetService: TimesheetService, private val userService: UserService) {
    fun fetchLeaderboard(callback: (List<Pair<User, Long>>, String) -> Unit){
        userService.fetchAllUsers{
            users, message ->
            val leaderboard = mutableListOf<Pair<User, Long>>()
            val countDownLatch = CountDownLatch(users.size)

            for (user in users) {
                timesheetService.getTimesheetByUserId(user.id) { tasks ->

                    val totalHours = tasks.sumOf {
                        var taskDuration = it.endTime - it.startTime
                        if (taskDuration < 0) {
                            taskDuration += 24 * 3600 * 1000 // Adding 24 hours in milliseconds
                        }
                        taskDuration
                    }
                    leaderboard.add(Pair(user, totalHours))
                    countDownLatch.countDown()
                }
            }

            GlobalScope.launch(Dispatchers.IO) {
                countDownLatch.await()
                leaderboard.sortByDescending { it.second }
                withContext(Dispatchers.Main) {
                    callback(leaderboard, message)
                }
            }
        }
    }

}