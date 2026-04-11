package com.surajpetwal.tasktracker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.surajpetwal.tasktracker.R

class MissedTasksIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var missedCount: Int = 0
    private val circleRadius = 12f // Radius in dp
    private val circleSpacing = 8f // Spacing between circles in dp
    
    init {
        paint.color = ContextCompat.getColor(context, R.color.task_missed)
        paint.style = Paint.Style.FILL
    }
    
    fun setMissedCount(count: Int) {
        missedCount = count
        visibility = if (count > 0) VISIBLE else GONE
        invalidate()
    }
    
    fun getMissedCount(): Int = missedCount
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val density = resources.displayMetrics.density
        val totalWidth = if (missedCount > 0) {
            ((missedCount * 2 * circleRadius) + ((missedCount - 1) * circleSpacing)) * density
        } else {
            0f
        }
        val height = (2 * circleRadius) * density
        
        setMeasuredDimension(
            resolveSize(totalWidth.toInt(), widthMeasureSpec),
            resolveSize(height.toInt(), heightMeasureSpec)
        )
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        if (missedCount <= 0) return
        
        val density = resources.displayMetrics.density
        val radius = circleRadius * density
        val spacing = circleSpacing * density
        val centerY = height / 2f
        
        for (i in 0 until missedCount.coerceAtMost(5)) { // Show max 5 circles
            val centerX = radius + (i * (2 * radius + spacing))
            canvas.drawCircle(centerX, centerY, radius, paint)
        }
        
        // If more than 5 missed tasks, show "+" indicator
        if (missedCount > 5) {
            val centerX = radius + (5 * (2 * radius + spacing))
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ContextCompat.getColor(context, R.color.task_missed)
                textSize = 16f * density
                textAlign = Paint.Align.LEFT
            }
            canvas.drawText("+", centerX - radius, centerY + (5f * density), textPaint)
        }
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        requestLayout()
    }
}
