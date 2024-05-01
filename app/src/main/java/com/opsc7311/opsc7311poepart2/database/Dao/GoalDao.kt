package com.opsc7311.opsc7311poepart2.database.Dao

import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.opsc7311.opsc7311poepart2.database.model.Goal

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goal: Goal)
    @Update
    fun update(goal: Goal)
    @Query("SELECT * FROM Goal WHERE userId = :userId")
    fun getGoalForUser(userId: Int): Goal?

    // Custom method to insert or update goal based on user's presence in the table
    @Transaction
    fun insertOrUpdate(goal: Goal) {
        val existingGoal = getGoalForUser(goal.userId)
        if (existingGoal != null) {
            // Update the existing goal
            goal.id = existingGoal.id // Ensure the new goal has the same id as the existing one
            update(goal)
        } else {
            // Insert a new goal
            insert(goal)
        }
    }
}