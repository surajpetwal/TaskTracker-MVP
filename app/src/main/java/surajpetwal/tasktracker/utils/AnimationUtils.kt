package com.surajpetwal.tasktracker.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView

object AnimationUtils {

    fun fadeIn(view: View, duration: Long = 300) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    fun fadeOut(view: View, duration: Long = 300, onComplete: (() -> Unit)? = null) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    onComplete?.invoke()
                }
            })
            .start()
    }

    fun slideUp(view: View, duration: Long = 300) {
        view.translationY = 100f
        view.alpha = 0f
        view.visibility = View.VISIBLE
        
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f)
        
        ObjectAnimator.ofPropertyValuesHolder(view, translationY, alpha).apply {
            this.duration = duration
            interpolator = OvershootInterpolator(1.5f)
            start()
        }
    }

    fun pulse(view: View, duration: Long = 200) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.1f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.1f, 1f)
        
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            this.duration = duration
            start()
        }
    }

    fun shake(view: View, duration: Long = 400) {
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        
        ObjectAnimator.ofPropertyValuesHolder(view, translationX).apply {
            this.duration = duration
            start()
        }
    }

    fun RecyclerView.animateItemInsertion(position: Int) {
        val view = layoutManager?.findViewByPosition(position) ?: return
        slideUp(view, 400)
    }

    fun View.bounceIn(duration: Long = 500) {
        scaleX = 0f
        scaleY = 0f
        alpha = 0f
        visibility = View.VISIBLE

        animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(OvershootInterpolator(2f))
            .start()
    }
}
