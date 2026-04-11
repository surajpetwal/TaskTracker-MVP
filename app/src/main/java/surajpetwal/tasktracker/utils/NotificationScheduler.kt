package com.surajpetwal.tasktracker.utils

import android.content.Context
import androidx.work.*
import com.surajpetwal.tasktracker.workers.TaskReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    companion object {
        const val DAILY_SUMMARY_WORK = "daily_summary_work"
        const val TASK_REMINDER_WORK = "task_reminder_work"
    }

    fun scheduleDailySummary(hour: Int, minute: Int) {
        val currentTime = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // If time has passed today, schedule for tomorrow
        if (scheduledTime.before(currentTime)) {
            scheduledTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = scheduledTime.timeInMillis - currentTime.timeInMillis

        val dailyWorkRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    TaskReminderWorker.KEY_NOTIFICATION_TYPE to TaskReminderWorker.TYPE_DAILY_SUMMARY
                )
            )
            .addTag(DAILY_SUMMARY_WORK)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_SUMMARY_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }

    fun scheduleTaskReminders() {
        // Schedule task reminders every 2 hours
        val taskReminderRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(2, TimeUnit.HOURS)
            .setInputData(
                workDataOf(
                    TaskReminderWorker.KEY_NOTIFICATION_TYPE to TaskReminderWorker.TYPE_TASK_REMINDER
                )
            )
            .addTag(TASK_REMINDER_WORK)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TASK_REMINDER_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            taskReminderRequest
        )
    }

    fun cancelDailySummary() {
        WorkManager.getInstance(context).cancelUniqueWork(DAILY_SUMMARY_WORK)
    }

    fun cancelTaskReminders() {
        WorkManager.getInstance(context).cancelUniqueWork(TASK_REMINDER_WORK)
    }

    fun cancelAllNotifications() {
        WorkManager.getInstance(context).cancelAllWork()
        NotificationHelper(context).apply {
            // Cancel all pending notifications
            for (i in 1..100) {
                cancelNotification(i)
            }
        }
    }

    fun isDailySummaryScheduled(): Boolean {
        val workManager = WorkManager.getInstance(context)
        val workInfo = workManager.getWorkInfosForUniqueWork(DAILY_SUMMARY_WORK).get()
        return workInfo?.any { 
            it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING 
        } ?: false
    }
}
