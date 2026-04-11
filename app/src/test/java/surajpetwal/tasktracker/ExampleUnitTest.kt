package com.surajpetwal.tasktracker

import com.surajpetwal.tasktracker.data.PointsManager
import com.surajpetwal.tasktracker.model.Task
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine.
 */
class ExampleUnitTest {
    
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun taskCreation_isCorrect() {
        val task = Task(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            points = 5
        )
        
        assertEquals(1, task.id)
        assertEquals("Test Task", task.title)
        assertEquals("Test Description", task.description)
        assertEquals(5, task.points)
        assertFalse(task.isCompleted)
    }
    
    @Test
    fun taskCompletion_togglesCorrectly() {
        val task = Task(
            title = "Test Task",
            points = 1
        )
        
        assertFalse(task.isCompleted)
        
        val completedTask = task.copy(isCompleted = true)
        assertTrue(completedTask.isCompleted)
    }
    
    @Test
    fun pointsCalculation_isCorrect() {
        val tasks = listOf(
            Task(title = "Task 1", points = 3, isCompleted = true),
            Task(title = "Task 2", points = 2, isCompleted = true),
            Task(title = "Task 3", points = 5, isCompleted = false)
        )
        
        val completedTasks = tasks.filter { it.isCompleted }
        val totalPoints = completedTasks.sumOf { it.points }
        
        assertEquals(2, completedTasks.size)
        assertEquals(5, totalPoints)
    }
}
