package com.surajpetwal.tasktracker.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatsCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    // Glassmorphism background
    val glassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x4DFFFFFF), // 30% transparent white
            Color(0x33FFFFFF)  // 20% transparent white
        )
    )
    
    Card(
        modifier = modifier
            .width(100.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x4DFFFFFF) // Glass surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(glassBrush)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF6A3D), // Primary color
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun TasksCompletedCard(
    completedTasks: Int,
    totalTasks: Int,
    modifier: Modifier = Modifier
) {
    StatsCard(
        icon = Icons.Default.CheckCircle,
        value = "$completedTasks/$totalTasks",
        label = "Tasks",
        modifier = modifier
    )
}

@Composable
fun StreakCard(
    streak: Int,
    modifier: Modifier = Modifier
) {
    StatsCard(
        icon = Icons.Default.LocalFireDepartment,
        value = "$streak",
        label = "Streak",
        modifier = modifier
    )
}

@Composable
fun PointsCard(
    points: Int,
    modifier: Modifier = Modifier
) {
    StatsCard(
        icon = Icons.Default.Stars,
        value = "$points",
        label = "Points",
        modifier = modifier
    )
}
