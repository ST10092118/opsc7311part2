package com.opsc7311.opsc7311poepart2.database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.opsc7311.opsc7311poepart2.database.model.User

@Dao
interface UserDao {

    @Insert
    fun registerUser(user: User)

    @Query("SELECT * FROM User WHERE username = :username and password = :password")

    fun loginUser(username: String, password: String): User?
}