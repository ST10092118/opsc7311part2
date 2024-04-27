package com.opsc7311.opsc7311poepart2.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)])

data class Goal(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val minimumTime: Int,
    val maximumTime: Int,
    val userId: Int
    )
