package com.example.habitflow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val isCompletedToday: Boolean = false,
    val creationDate: Long = System.currentTimeMillis()
)