package com.surajpetwal.tasktracker.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val points: Int = 1,
    val isCompleted: Boolean = false,
    val createdDate: String = "",
    val dueDate: String? = null,
    val dailyQuota: Int = 10,
    val isMissed: Boolean = false,
    val category: String = "General" // Day 9: Added category support
)
