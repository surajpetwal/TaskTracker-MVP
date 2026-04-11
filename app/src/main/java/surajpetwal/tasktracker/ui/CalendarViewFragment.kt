package com.surajpetwal.tasktracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tasktracker.R
import com.tasktracker.repository.TaskRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewFragment : Fragment() {
    
    private lateinit var taskRepository: TaskRepository
    private lateinit var calendarView: CalendarView
    private lateinit var tvSelectedDate: TextView
    private lateinit var rvTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_view, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        taskRepository = TaskRepository(requireContext())
        
        initViews(view)
        setupRecyclerView()
        setupCalendarView()
        
        // Load today's tasks by default
        val today = dateFormat.format(Date())
        loadTasksForDate(today)
    }
    
    private fun initViews(view: View) {
        calendarView = view.findViewById(R.id.calendarView)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)
        rvTasks = view.findViewById(R.id.rvTasks)
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
    
    private fun setupCalendarView() {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val selectedDate = dateFormat.format(calendar.time)
            
            // Update selected date display
            val displayFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            tvSelectedDate.text = displayFormat.format(calendar.time)
            
            // Load tasks for selected date
            loadTasksForDate(selectedDate)
        }
    }
    
    private fun loadTasksForDate(date: String) {
        lifecycleScope.launch {
            try {
                val tasks = taskRepository.getTasksByDate(date)
                taskAdapter.submitList(tasks)
                
                // Update selected date display if not already set
                if (tvSelectedDate.text.isEmpty()) {
                    val displayFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                    val parsedDate = dateFormat.parse(date)
                    tvSelectedDate.text = parsedDate?.let { displayFormat.format(it) } ?: date
                }
                
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
                
                // Reload tasks for current date
                val selectedDate = tvSelectedDate.text.toString()
                val dateForQuery = try {
                    val displayFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                    val parsedDate = displayFormat.parse(selectedDate)
                    parsedDate?.let { dateFormat.format(it) } ?: dateFormat.format(Date())
                } catch (e: Exception) {
                    dateFormat.format(Date())
                }
                loadTasksForDate(dateForQuery)
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
