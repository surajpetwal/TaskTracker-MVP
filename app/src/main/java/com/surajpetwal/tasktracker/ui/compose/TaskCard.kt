package com.surajpetwal.tasktracker.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.surajpetwal.tasktracker.R
import com.surajpetwal.tasktracker.model.Task
import com.surajpetwal.tasktracker.utils.CategoryManager

@Composable
fun TaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    
    // Get category color
    val categoryColor = CategoryManager.DEFAULT_CATEGORIES[task.category]?.let { colorRes ->
        Color(ContextCompat.getColor(context, colorRes))
    } ?: Color.Gray
    
    // Glassmorphism background
    val glassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x4DFFFFFF), // 30% transparent white
            Color(0x33FFFFFF)  // 20% transparent white
        )
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                isPressed = true
                onTaskClick(task)
            }
            .scale(if (isPressed) 0.98f else 1.0f),
        shape = RoundedCornerShape(20.dp), // 20dp corners as per Rework
        colors = CardDefaults.cardColors(
            containerColor = Color(0x4DFFFFFF) // Glass surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(glassBrush)
                .padding(16.dp), // 16dp padding as per Rework
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            IconButton(
                onClick = { onTaskClick(task) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.CheckCircleOutline
                    },
                    contentDescription = if (task.isCompleted) "Completed" else "Not completed",
                    tint = if (task.isCompleted) {
                        Color(0xFF4CAF50) // success color
                    } else {
                        Color(0xFF9E9E9E) // category_general
                    },
                    modifier = Modifier.alpha(if (task.isCompleted) 0.5f else 1.0f)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Task content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // Text on glass
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    modifier = Modifier.alpha(if (task.isCompleted) 0.5f else 1.0f)
                )
                
                // Description
                if (!task.description.isNullOrEmpty()) {
                    Text(
                        text = task.description,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (task.isCompleted) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        },
                        modifier = Modifier.alpha(if (task.isCompleted) 0.4f else 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Category chip (pill shape as per Rework)
                Surface(
                    modifier = Modifier.wrapContentWidth(),
                    shape = RoundedCornerShape(50), // Fully rounded pill
                    color = categoryColor.copy(alpha = if (task.isCompleted) 0.5f else 1.0f)
                ) {
                    Text(
                        text = task.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Points
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF6A3D) // Primary color
            ) {
                Text(
                    text = "${task.points} pts",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
    
    // Reset press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(200)
            isPressed = false
        }
    }
}
