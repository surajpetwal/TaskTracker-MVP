package com.surajpetwal.tasktracker.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.surajpetwal.tasktracker.R

abstract class TaskSwipeCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val backgroundPaint = Paint()
    private val iconPaint = Paint()
    
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val height = itemView.height.toFloat()
        val width = height / 3

        if (dX > 0) {
            // Swipe right - Complete (Green)
            backgroundPaint.color = Color.parseColor("#4CAF50")
            c.drawRect(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                dX,
                itemView.bottom.toFloat(),
                backgroundPaint
            )
            
            // Draw check icon
            iconPaint.color = Color.WHITE
            iconPaint.textSize = 40f
            iconPaint.textAlign = Paint.Align.CENTER
            c.drawText("✓",
                itemView.left + width,
                itemView.top + height / 2 + 15,
                iconPaint
            )
        } else if (dX < 0) {
            // Swipe left - Delete (Red)
            backgroundPaint.color = Color.parseColor("#F44336")
            c.drawRect(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                backgroundPaint
            )
            
            // Draw delete icon
            iconPaint.color = Color.WHITE
            iconPaint.textSize = 40f
            iconPaint.textAlign = Paint.Align.CENTER
            c.drawText("🗑",
                itemView.right - width,
                itemView.top + height / 2 + 15,
                iconPaint
            )
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    abstract fun onSwipedRight(position: Int)
    abstract fun onSwipedLeft(position: Int)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.RIGHT -> onSwipedRight(position)
            ItemTouchHelper.LEFT -> onSwipedLeft(position)
        }
    }
}
