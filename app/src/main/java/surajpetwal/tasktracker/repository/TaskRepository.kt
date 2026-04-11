package com.surajpetwal.tasktracker.repository

import android.content.Context
import com.surajpetwal.tasktracker.data.TaskDatabase
import com.surajpetwal.tasktracker.model.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskRepository(context: Context) {
    private val database = TaskDatabase(context)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    suspend fun insertTask(task: Task): Long {
        val taskWithTimestamp = task.copy(
            createdDate = if (task.createdDate.isEmpty()) {
                dateFormat.format(Date())
            } else task.createdDate
        )
        return database.insertTask(taskWithTimestamp)
    }

    suspend fun getAllTasks(): List<Task> {
        return database.getAllTasks()
    }

    suspend fun updateTask(task: Task): Int {
        return database.updateTask(task)
    }

    suspend fun deleteTask(id: Int): Int {
        return database.deleteTask(id)
    }

    suspend fun getTasksByDate(date: String): List<Task> {
        return database.getTasksByDate(date)
    }

    suspend fun getTodayTasks(): List<Task> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return getTasksByDate(today)
    }

    suspend fun getMissedTasks(): List<Task> {
        return database.getMissedTasks()
    }

    suspend fun markTaskAsCompleted(taskId: Int): Int {
        val tasks = getAllTasks()
        val task = tasks.find { it.id == taskId }
        return if (task != null) {
            val updatedTask = task.copy(isCompleted = true)
            updateTask(updatedTask)
        } else {
            0
        }
    }

    suspend fun markTaskAsMissed(taskId: Int): Int {
        val tasks = getAllTasks()
        val task = tasks.find { it.id == taskId }
        return if (task != null) {
            val updatedTask = task.copy(isMissed = true, isCompleted = false)
            updateTask(updatedTask)
        } else {
            0
        }
    }

    suspend fun getTodayCompletedTasks(): List<Task> {
        return getTodayTasks().filter { it.isCompleted }
    }

    suspend fun getTodayPendingTasks(): List<Task> {
        return getTodayTasks().filter { !it.isCompleted && !it.isMissed }
    }

    suspend fun getTodayPoints(): Int {
        return getTodayCompletedTasks().sumOf { it.points }
    }

    suspend fun getTotalPoints(): Int {
        return getAllTasks().filter { it.isCompleted }.sumOf { it.points }
    }
}
