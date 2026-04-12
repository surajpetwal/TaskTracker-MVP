package com.surajpetwal.tasktracker.data

import com.surajpetwal.tasktracker.model.Task

class TaskDao(private val database: TaskDatabase) {

    fun insertTask(task: Task): Long {
        return database.insertTask(task)
    }

    fun getAllTasks(): List<Task> {
        return database.getAllTasks()
    }

    fun updateTask(task: Task): Int {
        return database.updateTask(task)
    }

    fun deleteTask(id: Int): Int {
        return database.deleteTask(id)
    }

    fun getTasksByDate(date: String): List<Task> {
        return database.getTasksByDate(date)
    }

    fun getMissedTasks(): List<Task> {
        return database.getMissedTasks()
    }
}
