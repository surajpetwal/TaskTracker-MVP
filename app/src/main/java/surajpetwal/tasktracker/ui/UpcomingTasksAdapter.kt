package com.surajpetwal.tasktracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tasktracker.R
import java.text.SimpleDateFormat
import java.util.*

data class UpcomingDay(
    val date: String,
    val taskCount: Int,
    val totalPoints: Int
)

class UpcomingTasksAdapter : ListAdapter<UpcomingDay, UpcomingTasksAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUpcomingDay: TextView = itemView.findViewById(R.id.tvUpcomingDay)
        private val tvUpcomingDate: TextView = itemView.findViewById(R.id.tvUpcomingDate)
        private val tvUpcomingTaskCount: TextView = itemView.findViewById(R.id.tvUpcomingTaskCount)

        fun bind(upcomingDay: UpcomingDay) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val displayFormat = SimpleDateFormat("MMM d", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val today = calendar.time
            
            try {
                val date = dateFormat.parse(upcomingDay.date)
                calendar.time = date
                
                // Determine day label
                val dayLabel = when {
                    isSameDay(calendar, Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }) -> "Tomorrow"
                    isSameDay(calendar, Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) }) -> "Day After"
                    else -> {
                        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                        dayFormat.format(date)
                    }
                }
                
                tvUpcomingDay.text = dayLabel
                tvUpcomingDate.text = displayFormat.format(date)
                tvUpcomingTaskCount.text = "${upcomingDay.taskCount} tasks • ${upcomingDay.totalPoints}pts"
                
            } catch (e: Exception) {
                tvUpcomingDay.text = "Unknown"
                tvUpcomingDate.text = upcomingDay.date
                tvUpcomingTaskCount.text = "${upcomingDay.taskCount} tasks"
            }
        }

        private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                   cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UpcomingDay>() {
        override fun areItemsTheSame(oldItem: UpcomingDay, newItem: UpcomingDay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: UpcomingDay, newItem: UpcomingDay): Boolean {
            return oldItem == newItem
        }
    }
}
