package com.tasktracker.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class PointsManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "TaskTrackerPrefs"
        private const val KEY_DAILY_QUOTA = "daily_quota"
        private const val KEY_LAST_RESET_DATE = "last_reset_date"
        private const val DEFAULT_QUOTA = 10
    }
    
    // Daily Quota Management
    fun getDailyQuota(): Int {
        return prefs.getInt(KEY_DAILY_QUOTA, DEFAULT_QUOTA)
    }
    
    fun setDailyQuota(quota: Int) {
        prefs.edit().putInt(KEY_DAILY_QUOTA, quota).apply()
    }
    
    // Check if quota is met for given points
    fun isQuotaMet(earnedPoints: Int): Boolean {
        return earnedPoints >= getDailyQuota()
    }
    
    // Get quota progress percentage
    fun getQuotaProgress(earnedPoints: Int): Int {
        val quota = getDailyQuota()
        return if (quota > 0) {
            ((earnedPoints.toFloat() / quota.toFloat()) * 100).toInt().coerceIn(0, 100)
        } else {
            100
        }
    }
    
    // Calculate streak based on consecutive days meeting quota
    fun calculateStreak(completedDates: List<String>): Int {
        if (completedDates.isEmpty()) return 0
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sortedDates = completedDates.sorted()
        
        var streak = 0
        val today = Calendar.getInstance()
        
        // Check if streak is active (met quota today or yesterday)
        val lastCompletedDate = sortedDates.lastOrNull()
        if (lastCompletedDate != null) {
            val lastDate = dateFormat.parse(lastCompletedDate)
            val lastCalendar = Calendar.getInstance().apply { time = lastDate }
            
            // Check if last completion was today or yesterday
            val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
            
            if (isSameDay(lastCalendar, today) || isSameDay(lastCalendar, yesterday)) {
                streak = 1
                
                // Count backwards for consecutive days
                val checkDate = Calendar.getInstance().apply { 
                    time = lastDate
                    add(Calendar.DAY_OF_YEAR, -1) 
                }
                
                for (i in sortedDates.size - 2 downTo 0) {
                    val currentDate = dateFormat.parse(sortedDates[i])
                    val currentCalendar = Calendar.getInstance().apply { time = currentDate }
                    
                    if (isSameDay(currentCalendar, checkDate)) {
                        streak++
                        checkDate.add(Calendar.DAY_OF_YEAR, -1)
                    } else {
                        break
                    }
                }
            }
        }
        
        return streak
    }
    
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
    
    // Get quota status message
    fun getQuotaStatusMessage(earnedPoints: Int): String {
        return when {
            earnedPoints >= getDailyQuota() -> "Quota met! 🎉"
            earnedPoints >= getDailyQuota() / 2 -> "Getting closer! 💪"
            earnedPoints > 0 -> "Keep going! 📈"
            else -> "Start your day! 🚀"
        }
    }
    
    // Get progress color based on completion
    fun getProgressColor(earnedPoints: Int): Int {
        val progress = getQuotaProgress(earnedPoints)
        return when {
            progress >= 100 -> 0xFF4CAF50.toInt() // Green
            progress >= 50 -> 0xFFFF9800.toInt()  // Orange
            else -> 0xFFF44336.toInt()             // Red
        }
    }
}
