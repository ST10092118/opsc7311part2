package com.opsc7311.opsc7311poepart2.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.opsc7311.opsc7311poepart2.database.Dao.CategoryDao
import com.opsc7311.opsc7311poepart2.database.Dao.GoalDao
import com.opsc7311.opsc7311poepart2.database.Dao.TimesheetDao
import com.opsc7311.opsc7311poepart2.database.Dao.UserDao

abstract class ClockWorkDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun timesheetDao(): TimesheetDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: ClockWorkDatabase? = null

        fun getDatabase(context: Context): ClockWorkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClockWorkDatabase::class.java,
                    "clockwork_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}