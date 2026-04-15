package com.surajpetwal.tasktracker.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.model.Task
import com.surajpetwal.tasktracker.repository.TaskRepository
import com.surajpetwal.tasktracker.utils.CategoryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskDialog(context: Context, private val onTaskAdded: (Task) -> Unit) : Dialog(context) {
    
    private lateinit var etTaskTitle: TextInputEditText
    private lateinit var etTaskDescription: TextInputEditText
    private lateinit var etTaskPoints: TextInputEditText
    private lateinit var spinnerCategory: AutoCompleteTextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    
    private val taskRepository = TaskRepository(context)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_task)
        
        initViews()
        setupClickListeners()
    }
    
    private fun initViews() {
        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        etTaskPoints = findViewById(R.id.etTaskPoints)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        
        // Day 9: Setup category dropdown
        val categories = CategoryManager.getAllCategories()
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, categories)
        spinnerCategory.setAdapter(adapter)
        spinnerCategory.setText(categories[0], false) // Default to first category (General)
    }
    
    private fun setupClickListeners() {
        btnSave.setOnClickListener {
            saveTask()
        }
        
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
    
    private fun saveTask() {
        val title = etTaskTitle.text.toString().trim()
        val description = etTaskDescription.text.toString().trim()
        val pointsText = etTaskPoints.text.toString().trim()
        
        if (title.isEmpty()) {
            etTaskTitle.error = "Task title is required"
            return
        }
        
        val points = try {
            pointsText.toInt()
        } catch (e: NumberFormatException) {
            1 // Default to 1 point if invalid input
        }
        
        val category = spinnerCategory.text.toString().ifEmpty { "General" }
        
        val newTask = Task(
            title = title,
            description = description.ifEmpty { null },
            points = points,
            category = category
        )
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val taskId = withContext(Dispatchers.IO) {
                    taskRepository.insertTask(newTask)
                }
                onTaskAdded(newTask.copy(id = taskId.toInt()))
                dismiss()
                Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error adding task: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
