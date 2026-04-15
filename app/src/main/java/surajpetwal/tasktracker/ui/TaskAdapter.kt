package com.surajpetwal.tasktracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.model.Task
import com.surajpetwal.tasktracker.utils.CategoryManager

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getTaskAt(position: Int): Task? {
        return if (position in 0 until itemCount) {
            getItem(position)
        } else null
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
        private val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)

        fun bind(task: Task) {
            cbCompleted.isChecked = task.isCompleted
            tvTitle.text = task.title
            tvDescription.text = task.description ?: ""
            tvPoints.text = "${task.points} pts"
            
            // Day 9: Set category chip
            chipCategory.text = task.category
            val categoryColor = CategoryManager.getCategoryColor(itemView.context, task.category)
            chipCategory.setChipBackgroundColorResource(CategoryManager.DEFAULT_CATEGORIES[task.category] ?: R.color.category_general)
            
            // Set text appearance based on completion status
            if (task.isCompleted) {
                tvTitle.alpha = 0.6f
                tvDescription.alpha = 0.6f
                tvPoints.alpha = 0.6f
            } else {
                tvTitle.alpha = 1.0f
                tvDescription.alpha = 0.8f
                tvPoints.alpha = 1.0f
            }
            
            // Set click listeners
            itemView.setOnClickListener {
                onTaskClick(task)
            }
            
            cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != task.isCompleted) {
                    onTaskClick(task)
                }
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
