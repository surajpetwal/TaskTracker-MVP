package com.surajpetwal.tasktracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.dialog.AddTaskDialog
import com.surajpetwal.tasktracker.repository.TaskRepository
import com.surajpetwal.tasktracker.utils.CategoryManager
import kotlinx.coroutines.launch

class DailyViewFragment : Fragment() {
    
    private lateinit var taskRepository: TaskRepository
    private lateinit var tvDate: TextView
    private lateinit var tvTaskCount: TextView
    private lateinit var tvPoints: TextView
    private lateinit var rvTasks: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var layoutMissedIndicator: LinearLayout
    private lateinit var missedTasksIndicator: MissedTasksIndicator
    private lateinit var rvUpcomingTasks: RecyclerView
    private lateinit var upcomingTasksAdapter: UpcomingTasksAdapter
    
    // Day 9: Category filter
    private lateinit var categoryFilterGroup: ChipGroup
    private var selectedCategory: String? = null
    
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
        fabAddTask = view.findViewById(R.id.fabAddTask)
        layoutMissedIndicator = view.findViewById(R.id.layoutMissedIndicator)
        missedTasksIndicator = view.findViewById(R.id.missedTasksIndicator)
        rvUpcomingTasks = view.findViewById(R.id.rvUpcomingTasks)
        
        // Day 9: Initialize category filter
        val categoryFilterView = view.findViewById<View>(R.id.categoryFilter)
        categoryFilterGroup = categoryFilterView.findViewById(R.id.chipGroupFilter)
        setupCategoryFilter()
        
        // Set current date
        val currentDate = java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())
        tvDate.text = currentDate
        
        // Setup FAB click listener
        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }
    
    private fun showAddTaskDialog() {
        val dialog = AddTaskDialog(requireContext()) { newTask ->
            // Refresh task list after adding new task
            loadTodayTasks()
        }
        dialog.show()
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
        
        // Setup swipe actions
        setupSwipeActions()
        
        // Setup upcoming tasks RecyclerView
        upcomingTasksAdapter = UpcomingTasksAdapter()
        rvUpcomingTasks.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingTasksAdapter
        }
    }
    
    private fun setupCategoryFilter() {
        // Clear existing chips
        categoryFilterGroup.removeAllViews()
        
        // Add "All" chip
        val allChip = Chip(requireContext())
        allChip.text = "All"
        allChip.isCheckable = true
        allChip.isChecked = true
        allChip.setOnClickListener {
            selectedCategory = null
            loadTodayTasks()
        }
        categoryFilterGroup.addView(allChip)
        
        // Add category chips
        CategoryManager.getAllCategories().forEach { category ->
            val chip = Chip(requireContext())
            chip.text = category
            chip.isCheckable = true
            chip.setChipBackgroundColorResource(CategoryManager.DEFAULT_CATEGORIES[category] ?: R.color.category_general)
            chip.setTextColor(requireContext().getColor(android.R.color.white))
            chip.setOnClickListener {
                // Uncheck "All" chip when a category is selected
                allChip.isChecked = false
                selectedCategory = if (selectedCategory == category) null else category
                chip.isChecked = selectedCategory == category
                loadTodayTasks()
            }
            categoryFilterGroup.addView(chip)
        }
    }
    
    private fun loadTodayTasks() {
        lifecycleScope.launch {
            try {
                var todayTasks = taskRepository.getTodayTasks()
                
                // Day 9: Filter by selected category
                selectedCategory?.let { category ->
                    todayTasks = todayTasks.filter { it.category == category }
                }
                
                val completedTasks = todayTasks.filter { it.isCompleted }
                val todayPoints = taskRepository.getTodayPoints()
                
                tvTaskCount.text = "${completedTasks.size}/${todayTasks.size} tasks completed"
                tvPoints.text = "$todayPoints points today"
                
                taskAdapter.submitList(todayTasks)
                
                // Load and display missed tasks count
                val missedTasks = taskRepository.getMissedTasks()
                if (missedTasks.isNotEmpty()) {
                    layoutMissedIndicator.visibility = View.VISIBLE
                    missedTasksIndicator.setMissedCount(missedTasks.size)
                } else {
                    layoutMissedIndicator.visibility = View.GONE
                }
                
                // Load upcoming tasks
                loadUpcomingTasks()
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private suspend fun loadUpcomingTasks() {
        val allTasks = taskRepository.getAllTasks()
        val upcomingDays = mutableListOf<UpcomingDay>()
        
        // Group tasks by future dates
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(calendar.time)
        
        // Check next 7 days
        for (i in 1..7) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val checkDate = dateFormat.format(calendar.time)
            
            val tasksForDay = allTasks.filter { task ->
                task.dueDate?.startsWith(checkDate) == true && !task.isCompleted
            }
            
            if (tasksForDay.isNotEmpty()) {
                val totalPoints = tasksForDay.sumOf { it.points }
                upcomingDays.add(UpcomingDay(checkDate, tasksForDay.size, totalPoints))
            }
        }
        
        upcomingTasksAdapter.submitList(upcomingDays)
    }
    
    private fun setupSwipeActions() {
        val swipeCallback = object : TaskSwipeCallback() {
            override fun onSwipedRight(position: Int) {
                // Complete task
                val task = taskAdapter.getTaskAt(position)
                if (task != null && !task.isCompleted) {
                    lifecycleScope.launch {
                        try {
                            val updatedTask = task.copy(isCompleted = true)
                            taskRepository.updateTask(updatedTask)
                            loadTodayTasks()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onSwipedLeft(position: Int) {
                // Delete task
                val task = taskAdapter.getTaskAt(position)
                if (task != null) {
                    lifecycleScope.launch {
                        try {
                            taskRepository.deleteTask(task.id)
                            loadTodayTasks()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(rvTasks)
    }
    
    private fun toggleTaskCompletion(task: com.surajpetwal.tasktracker.model.Task) {
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
