package com.surajpetwal.tasktracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
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
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
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
            
            // Day 10: Enhanced category chip styling
            chipCategory.text = task.category
            val categoryColor = CategoryManager.getCategoryColor(itemView.context, task.category)
            chipCategory.setChipBackgroundColorResource(CategoryManager.DEFAULT_CATEGORIES[task.category] ?: R.color.category_general)
            chipCategory.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            
            // Day 10: Improved visual hierarchy with better alpha management
            if (task.isCompleted) {
                tvTitle.alpha = 0.5f
                tvDescription.alpha = 0.4f
                tvPoints.alpha = 0.5f
                chipCategory.alpha = 0.5f
                cardView.alpha = 0.8f
                
                // Strike through completed tasks
                tvTitle.paintFlags = tvTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                tvDescription.paintFlags = tvDescription.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTitle.alpha = 1.0f
                tvDescription.alpha = 0.7f
                tvPoints.alpha = 1.0f
                chipCategory.alpha = 1.0f
                cardView.alpha = 1.0f
                
                // Remove strike through
                tvTitle.paintFlags = tvTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvDescription.paintFlags = tvDescription.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            
            // Day 10: Enhanced interaction feedback with animation
            itemView.setOnClickListener {
                // Add press animation feedback
                itemView.animate()
                    .scaleX(0.98f)
                    .scaleY(0.98f)
                    .setDuration(100)
                    .withEndAction {
                        itemView.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start()
                    }
                    .start()
                
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
