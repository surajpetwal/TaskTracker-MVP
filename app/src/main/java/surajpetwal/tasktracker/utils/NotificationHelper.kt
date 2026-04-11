package com.surajpetwal.tasktracker.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.surajpetwal.tasktracker.MainActivity
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.model.Task

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID_TASKS = "task_reminders"
        const val CHANNEL_ID_DAILY = "daily_reminders"
        const val NOTIFICATION_GROUP_TASKS = "com.surajpetwal.tasktracker.TASKS"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val taskChannel = NotificationChannel(
                CHANNEL_ID_TASKS,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming and overdue tasks"
            }

            val dailyChannel = NotificationChannel(
                CHANNEL_ID_DAILY,
                "Daily Summary",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily task summary and reminders"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannels(listOf(taskChannel, dailyChannel))
        }
    }

    fun showTaskReminder(task: Task) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("task_id", task.id)
            putExtra("date", task.date)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TASKS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Task Reminder: ${task.title}")
            .setContentText(task.description ?: "You have a task due today!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(NOTIFICATION_GROUP_TASKS)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(task.id, notification)
        } catch (e: SecurityException) {
            // Notification permission not granted
            e.printStackTrace()
        }
    }

    fun showDailySummary(totalTasks: Int, completedTasks: Int, pendingTasks: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentText = when {
            pendingTasks == 0 -> "Great job! All tasks completed for today! 🎉"
            pendingTasks == 1 -> "1 task pending. You can do it! 💪"
            else -> "$pendingTasks tasks pending. Keep going! 💪"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_DAILY)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Daily Task Summary")
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Completed: $completedTasks/$totalTasks\n$contentText"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(1000, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}
