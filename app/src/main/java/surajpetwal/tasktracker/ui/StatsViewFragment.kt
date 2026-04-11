package com.surajpetwal.tasktracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tasktracker.R
import com.tasktracker.data.PointsManager
import com.tasktracker.repository.TaskRepository
import kotlinx.coroutines.launch

class StatsViewFragment : Fragment() {
    
    private lateinit var taskRepository: TaskRepository
    private lateinit var pointsManager: PointsManager
    private lateinit var tvTotalTasks: TextView
    private lateinit var tvCompletedTasks: TextView
    private lateinit var tvTotalPoints: TextView
    private lateinit var tvTodayPoints: TextView
    private lateinit var tvMissedTasks: TextView
    private lateinit var tvStreak: TextView
    private lateinit var tvQuotaStatus: TextView
    private lateinit var tvQuotaProgress: TextView
    private lateinit var progressBarQuota: ProgressBar
    private lateinit var cardQuota: CardView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats_view, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        taskRepository = TaskRepository(requireContext())
        pointsManager = PointsManager(requireContext())
        
        initViews(view)
        loadStats()
    }
    
    private fun initViews(view: View) {
        tvTotalTasks = view.findViewById(R.id.tvTotalTasks)
        tvCompletedTasks = view.findViewById(R.id.tvCompletedTasks)
        tvTotalPoints = view.findViewById(R.id.tvTotalPoints)
        tvTodayPoints = view.findViewById(R.id.tvTodayPoints)
        tvMissedTasks = view.findViewById(R.id.tvMissedTasks)
        tvStreak = view.findViewById(R.id.tvStreak)
        
        // Quota views
        tvQuotaStatus = view.findViewById(R.id.tvQuotaStatus)
        tvQuotaProgress = view.findViewById(R.id.tvQuotaProgress)
        progressBarQuota = view.findViewById(R.id.progressBarQuota)
        cardQuota = view.findViewById(R.id.cardQuota)
    }
    
    private fun loadStats() {
        lifecycleScope.launch {
            try {
                val allTasks = taskRepository.getAllTasks()
                val completedTasks = allTasks.filter { it.isCompleted }
                val missedTasks = taskRepository.getMissedTasks()
                val totalPoints = taskRepository.getTotalPoints()
                val todayPoints = taskRepository.getTodayPoints()
                
                tvTotalTasks.text = "${allTasks.size}"
                tvCompletedTasks.text = "${completedTasks.size}"
                tvTotalPoints.text = "$totalPoints"
                tvTodayPoints.text = "$todayPoints"
                tvMissedTasks.text = "${missedTasks.size}"
                
                // Calculate streak
                val streak = calculateStreak(completedTasks)
                tvStreak.text = "$streak days"
                
                // Update quota progress
                val quotaProgress = pointsManager.getQuotaProgress(todayPoints)
                progressBarQuota.progress = quotaProgress
                tvQuotaProgress.text = "$todayPoints / ${pointsManager.getDailyQuota()} points"
                tvQuotaStatus.text = pointsManager.getQuotaStatusMessage(todayPoints)
                
                // Change card color based on quota completion
                if (pointsManager.isQuotaMet(todayPoints)) {
                    cardQuota.setCardBackgroundColor(resources.getColor(R.color.task_completed, null))
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun calculateStreak(completedTasks: List<com.tasktracker.model.Task>): Int {
        if (completedTasks.isEmpty()) return 0
        
        // Simple streak calculation - count unique days with completed tasks
        val uniqueDays = completedTasks
            .map { it.createdDate.substring(0, 10) } // Extract date part
            .distinct()
            .sorted()
        
        var streak = 0
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())
        
        // Check backwards from today
        val calendar = java.util.Calendar.getInstance()
        for (i in 0 until 30) { // Check last 30 days
            val checkDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(calendar.time)
            
            if (uniqueDays.contains(checkDate)) {
                streak++
            } else if (i > 0) { // Allow for today not having tasks yet
                break
            }
            
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -1)
        }
        
        return streak
    }
}
