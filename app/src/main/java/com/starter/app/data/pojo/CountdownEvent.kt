package com.starter.app.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "countdown_events")
data class CountdownEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val targetDate: Date,
    val targetTime: String? = null,
    val recurrence: String? = null,
    val themeColor: String = "#FF5722",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isActive: Boolean = true,
    val notificationEnabled: Boolean = true,
    val reminderTime: Long = 24 * 60 * 60 * 1000 // 24 hours before event
)