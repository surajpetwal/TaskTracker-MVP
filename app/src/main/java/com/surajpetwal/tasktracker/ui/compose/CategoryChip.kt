package com.surajpetwal.tasktracker.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.utils.CategoryManager

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Get category color
    val categoryColor = CategoryManager.DEFAULT_CATEGORIES[category]?.let { colorRes ->
        Color(ContextCompat.getColor(context, colorRes))
    } ?: Color.Gray
    
    Surface(
        modifier = modifier
            .clickable { onCategorySelected(category) },
        shape = RoundedCornerShape(50), // Fully rounded pill as per Rework
        color = if (isSelected) {
            categoryColor // Selected state = filled
        } else {
            Color.Transparent // Unselected = transparent
        },
        border = if (!isSelected) {
            BorderStroke(1.dp, categoryColor) // Unselected = outline
        } else {
            null
        }
    ) {
        Text(
            text = category,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) {
                Color.White
            } else {
                categoryColor
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
