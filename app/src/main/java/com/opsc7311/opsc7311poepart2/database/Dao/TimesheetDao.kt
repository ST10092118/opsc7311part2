package com.opsc7311.opsc7311poepart2.database.Dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TimesheetDao {
    @Insert
    fun insertTimesheet(timesheet: Timesheet)

    @Query("SELECT * FROM Timesheet WHERE id = :timesheetId")
    fun getTimesheetById(timesheetId: Int): Timesheet?


    @Query("SELECT * FROM Timesheet WHERE userId = :userId")
    fun getTimesheetsForUser(userId: Int): Flow<List<Timesheet>>

    @Query("SELECT * FROM Timesheet WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    fun getTimesheetsBetweenDatesForUser(userId: Int, startDate: Date, endDate: Date): Flow<List<Timesheet>>
}