package com.tasktracker.utils

import android.content.Context
import android.util.Log
import com.tasktracker.model.Task
import com.tasktracker.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseTest(private val context: Context) {
    
    private val repository = TaskRepository(context)
    private val scope = CoroutineScope(Dispatchers.IO)
    
    fun runAllTests() {
        scope.launch {
            try {
                Log.d("DatabaseTest", "Starting database tests...")
                
                // Test 1: Insert tasks
                testInsertTasks()
                
                // Test 2: Get all tasks
                testGetAllTasks()
                
                // Test 3: Get today's tasks
                testGetTodayTasks()
                
                // Test 4: Update task
                testUpdateTask()
                
                // Test 5: Delete task
                testDeleteTask()
                
                // Test 6: Points calculation
                testPointsCalculation()
                
                Log.d("DatabaseTest", "All database tests completed successfully!")
                
            } catch (e: Exception) {
                Log.e("DatabaseTest", "Database test failed: ${e.message}", e)
            }
        }
    }
    
    private suspend fun testInsertTasks() {
        val task1 = Task(
            title = "Morning Exercise",
            description = "30 minutes cardio",
            points = 5
        )
        
        val task2 = Task(
            title = "Read Book",
            description = "Read 20 pages",
            points = 3
        )
        
        val id1 = repository.insertTask(task1)
        val id2 = repository.insertTask(task2)
        
        Log.d("DatabaseTest", "Inserted tasks with IDs: $id1, $id2")
    }
    
    private suspend fun testGetAllTasks() {
        val tasks = repository.getAllTasks()
        Log.d("DatabaseTest", "Total tasks in database: ${tasks.size}")
        tasks.forEach { task ->
            Log.d("DatabaseTest", "Task: ${task.title} - Completed: ${task.isCompleted}")
        }
    }
    
    private suspend fun testGetTodayTasks() {
        val todayTasks = repository.getTodayTasks()
        Log.d("DatabaseTest", "Today's tasks: ${todayTasks.size}")
    }
    
    private suspend fun testUpdateTask() {
        val tasks = repository.getAllTasks()
        if (tasks.isNotEmpty()) {
            val firstTask = tasks.first()
            val updatedTask = firstTask.copy(isCompleted = true)
            val result = repository.updateTask(updatedTask)
            Log.d("DatabaseTest", "Updated task result: $result")
        }
    }
    
    private suspend fun testDeleteTask() {
        val tasks = repository.getAllTasks()
        if (tasks.size > 1) {
            val taskToDelete = tasks[1]
            val result = repository.deleteTask(taskToDelete.id)
            Log.d("DatabaseTest", "Deleted task result: $result")
        }
    }
    
    private suspend fun testPointsCalculation() {
        val todayPoints = repository.getTodayPoints()
        val totalPoints = repository.getTotalPoints()
        Log.d("DatabaseTest", "Today's points: $todayPoints, Total points: $totalPoints")
    }
}
