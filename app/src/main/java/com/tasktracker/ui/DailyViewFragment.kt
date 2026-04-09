package com.tasktracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tasktracker.R
import com.tasktracker.repository.TaskRepository
import kotlinx.coroutines.launch

class DailyViewFragment : Fragment() {
    
    private lateinit var taskRepository: TaskRepository
    private lateinit var tvDate: TextView
    private lateinit var tvTaskCount: TextView
    private lateinit var tvPoints: TextView
    private lateinit var rvTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_view, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        taskRepository = TaskRepository(requireContext())
        
        initViews(view)
        setupRecyclerView()
        loadTodayTasks()
    }
    
    private fun initViews(view: View) {
        tvDate = view.findViewById(R.id.tvDate)
        tvTaskCount = view.findViewById(R.id.tvTaskCount)
        tvPoints = view.findViewById(R.id.tvPoints)
        rvTasks = view.findViewById(R.id.rvTasks)
        
        // Set current date
        val currentDate = java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())
        tvDate.text = currentDate
    }
    
    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter { task ->
            // Handle task click - toggle completion
            toggleTaskCompletion(task)
        }
        
        rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }
    
    private fun loadTodayTasks() {
        lifecycleScope.launch {
            try {
                val todayTasks = taskRepository.getTodayTasks()
                val completedTasks = todayTasks.filter { it.isCompleted }
                val todayPoints = taskRepository.getTodayPoints()
                
                tvTaskCount.text = "${completedTasks.size}/${todayTasks.size} tasks completed"
                tvPoints.text = "$todayPoints points today"
                
                taskAdapter.submitList(todayTasks)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun toggleTaskCompletion(task: com.tasktracker.model.Task) {
        lifecycleScope.launch {
            try {
                val updatedTask = task.copy(isCompleted = !task.isCompleted)
                taskRepository.updateTask(updatedTask)
                loadTodayTasks() // Refresh the list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
