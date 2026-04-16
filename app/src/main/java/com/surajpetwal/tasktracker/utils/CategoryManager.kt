package com.surajpetwal.tasktracker.utils

import com.surajpetwal.tasktracker.R

object CategoryManager {
    // Default categories with their color resources
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
    
    // Get all category names
    fun getAllCategories(): List<String> {
        return DEFAULT_CATEGORIES.keys.toList()
    }
    
    // Get color resource for a category
    fun getCategoryColorRes(category: String): Int {
        return DEFAULT_CATEGORIES[category] ?: R.color.category_general
    }
}
