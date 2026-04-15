package com.surajpetwal.tasktracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.dialog.AddTaskDialog
import com.surajpetwal.tasktracker.repository.TaskRepository
import com.surajpetwal.tasktracker.ui.compose.CategoryChip
import com.surajpetwal.tasktracker.ui.compose.PointsCard
import com.surajpetwal.tasktracker.ui.compose.StreakCard
import com.surajpetwal.tasktracker.ui.compose.SummaryCard
import com.surajpetwal.tasktracker.ui.compose.TasksCompletedCard
import com.surajpetwal.tasktracker.utils.CategoryManager
import kotlinx.coroutines.launch

class DailyViewFragment : Fragment() {
    
    private lateinit var taskRepository: TaskRepository
    private lateinit var tvGreeting: TextView
    private lateinit var tvDate: TextView
    private lateinit var rvTasks: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter
    
    // Compose views
    private lateinit var summaryCardView: ComposeView
    private lateinit var tasksCompletedCardView: ComposeView
    private lateinit var streakCardView: ComposeView
    private lateinit var pointsCardView: ComposeView
    private lateinit var categoryFilterRowView: ComposeView
    
    // Legacy views (for backward compatibility)
    private lateinit var layoutMissedIndicator: LinearLayout
    private lateinit var missedTasksIndicator: MissedTasksIndicator
    private lateinit var rvUpcomingTasks: RecyclerView
    private lateinit var upcomingTasksAdapter: UpcomingTasksAdapter
    
    // Category filter state
    private var selectedCategory: String? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_view_new, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        taskRepository = TaskRepository(requireContext())
        
        initViews(view)
        setupRecyclerView()
        loadTodayTasks()
    }
    
    private fun initViews(view: View) {
        // New layout views
        tvGreeting = view.findViewById(R.id.tvGreeting)
        tvDate = view.findViewById(R.id.tvDate)
        rvTasks = view.findViewById(R.id.rvTasks)
        fabAddTask = view.findViewById(R.id.fabAddTask)
        
        // Compose views
        summaryCardView = view.findViewById(R.id.summaryCard).findViewById(R.id.composeView)
        tasksCompletedCardView = view.findViewById(R.id.tasksCompletedCard).findViewById(R.id.composeView)
        streakCardView = view.findViewById(R.id.streakCard).findViewById(R.id.composeView)
        pointsCardView = view.findViewById(R.id.pointsCard).findViewById(R.id.composeView)
        categoryFilterRowView = view.findViewById(R.id.categoryFilterRow)
        
        // Legacy views (for backward compatibility - may not exist in new layout)
        layoutMissedIndicator = view.findViewById(R.id.layoutMissedIndicator)
        missedTasksIndicator = view.findViewById(R.id.missedTasksIndicator)
        rvUpcomingTasks = view.findViewById(R.id.rvUpcomingTasks)
        
        // Set greeting and current date
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> "Good Morning!"
            in 12..17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
        tvGreeting.text = greeting
        
        val currentDate = java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())
        tvDate.text = currentDate
        
        // Setup FAB click listener
        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
        
        // Setup Compose components
        setupComposeComponents()
    }
    
    private fun showAddTaskDialog() {
        val dialog = AddTaskDialog(requireContext()) { newTask ->
            // Refresh task list after adding new task
            loadTodayTasks()
        }
        dialog.show()
    }
    
    private fun setupComposeComponents() {
        // Setup category filter row with horizontal scroll
        categoryFilterRowView.setContent {
            androidx.compose.foundation.layout.Row(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.layout.ScrollState(0)),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                // "All" chip
                CategoryChip(
                    category = "All",
                    isSelected = selectedCategory == null,
                    onCategorySelected = { category ->
                        selectedCategory = if (category == "All") null else category
                        loadTodayTasks()
                    }
                )
                
                // Category chips
                CategoryManager.getAllCategories().forEach { category ->
                    CategoryChip(
                        category = category,
                        isSelected = selectedCategory == category,
                        onCategorySelected = { selected ->
                            selectedCategory = if (selected == category) null else category
                            loadTodayTasks()
                        }
                    )
                }
            }
        }
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
    
    private fun loadTodayTasks() {
        lifecycleScope.launch {
            try {
                var todayTasks = taskRepository.getTodayTasks()
                
                // Filter by selected category
                selectedCategory?.let { category ->
                    todayTasks = todayTasks.filter { it.category == category }
                }
                
                val completedTasks = todayTasks.filter { it.isCompleted }
                val todayPoints = taskRepository.getTodayPoints()
                val progress = if (todayTasks.isNotEmpty()) {
                    completedTasks.size.toFloat() / todayTasks.size
                } else {
                    0f
                }
                
                // Update Compose components
                summaryCardView.setContent {
                    SummaryCard(
                        progress = progress,
                        quote = "Keep going! You're doing great!",
                        score = todayPoints
                    )
                }
                
                tasksCompletedCardView.setContent {
                    TasksCompletedCard(
                        completedTasks = completedTasks.size,
                        totalTasks = todayTasks.size
                    )
                }
                
                streakCardView.setContent {
                    StreakCard(
                        streak = getStreak() // Calculate streak
                    )
                }
                
                pointsCardView.setContent {
                    PointsCard(
                        points = todayPoints
                    )
                }
                
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
    
    private fun getStreak(): Int {
        // Simple streak calculation - can be enhanced
        return 3 // Placeholder for actual streak calculation
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
