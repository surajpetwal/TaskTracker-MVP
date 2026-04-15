package com.surajpetwal.tasktracker.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.surajpetwal.tasktracker.R

object CategoryManager {
    
    // Default categories with colors
    val DEFAULT_CATEGORIES = mapOf(
        "General" to R.color.category_general,
        "Work" to R.color.category_work,
        "Personal" to R.color.category_personal,
        "Health" to R.color.category_health,
        "Study" to R.color.category_study,
        "Shopping" to R.color.category_shopping,
        "Finance" to R.color.category_finance,
        "Social" to R.color.category_social
    )
    
    fun getCategoryColor(context: Context, category: String): Int {
        val colorRes = DEFAULT_CATEGORIES[category] ?: R.color.category_general
        return ContextCompat.getColor(context, colorRes)
    }
    
    fun getAllCategories(): List<String> {
        return DEFAULT_CATEGORIES.keys.toList()
    }
    
    fun isValidCategory(category: String): Boolean {
        return DEFAULT_CATEGORIES.containsKey(category)
    }
}
