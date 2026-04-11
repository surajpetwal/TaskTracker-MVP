package com.surajpetwal.tasktracker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.surajpetwal.tasktracker.utils.NotificationScheduler

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device rebooted - rescheduling notifications")
            
            // Reschedule notifications
            val scheduler = NotificationScheduler(context)
            scheduler.scheduleTaskReminders()
            
            // Schedule daily summary for 9 AM (default time)
            scheduler.scheduleDailySummary(9, 0)
            
            Log.d("BootReceiver", "Notifications rescheduled successfully")
        }
    }
}
