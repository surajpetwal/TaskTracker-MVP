package com.surajpetwal.tasktracker.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.surajpetwal.tasktracker.repository.TaskRepository
import com.surajpetwal.tasktracker.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TaskReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "task_reminder_work"
        const val KEY_NOTIFICATION_TYPE = "notification_type"
        const val TYPE_TASK_REMINDER = "task_reminder"
        const val TYPE_DAILY_SUMMARY = "daily_summary"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val notificationType = inputData.getString(KEY_NOTIFICATION_TYPE) ?: TYPE_TASK_REMINDER
            val taskRepository = TaskRepository(applicationContext)
            val notificationHelper = NotificationHelper(applicationContext)

            when (notificationType) {
                TYPE_DAILY_SUMMARY -> {
                    showDailySummary(taskRepository, notificationHelper)
                }
                TYPE_TASK_REMINDER -> {
                    checkAndShowTaskReminders(taskRepository, notificationHelper)
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun showDailySummary(
        taskRepository: TaskRepository,
        notificationHelper: NotificationHelper
    ) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val tasks = taskRepository.getTasksByDate(today)

        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.isCompleted }
        val pendingTasks = totalTasks - completedTasks

        if (totalTasks > 0) {
            notificationHelper.showDailySummary(totalTasks, completedTasks, pendingTasks)
        }
    }

    private suspend fun checkAndShowTaskReminders(
        taskRepository: TaskRepository,
        notificationHelper: NotificationHelper
    ) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val tasks = taskRepository.getTasksByDate(today)

        // Show notifications for pending tasks
        tasks.filter { !it.isCompleted }.forEach { task ->
            notificationHelper.showTaskReminder(task)
        }
    }
}
