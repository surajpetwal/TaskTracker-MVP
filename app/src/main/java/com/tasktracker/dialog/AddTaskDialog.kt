package com.tasktracker.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.tasktracker.R
import com.tasktracker.model.Task
import com.tasktracker.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskDialog(context: Context, private val onTaskAdded: (Task) -> Unit) : Dialog(context) {
    
    private lateinit var etTaskTitle: TextInputEditText
    private lateinit var etTaskDescription: TextInputEditText
    private lateinit var etTaskPoints: TextInputEditText
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
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
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
        
        val newTask = Task(
            title = title,
            description = description.ifEmpty { null },
            points = points
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
