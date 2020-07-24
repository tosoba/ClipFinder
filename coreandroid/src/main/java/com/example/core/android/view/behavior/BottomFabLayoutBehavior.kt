package com.example.core.android.view.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class BottomFabLayoutBehavior(
    context: Context?,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {
    
    override fun layoutDependsOn(
        parent: CoordinatorLayout, child: FloatingActionButton, dependency: View
    ): Boolean = dependency is Snackbar.SnackbarLayout

    override fun onDependentViewRemoved(
        parent: CoordinatorLayout, child: FloatingActionButton, dependency: View
    ) {
        child.translationY = 0.0f
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: FloatingActionButton, dependency: View
    ): Boolean = updateButton(child, dependency)

    private fun updateButton(child: View, dependency: View): Boolean = if (
        dependency is Snackbar.SnackbarLayout
    ) {
        val oldTranslation = child.translationY
        val height = dependency.getHeight().toFloat()
        val newTranslation = dependency.getTranslationY() - height
        child.translationY = newTranslation
        oldTranslation != newTranslation
    } else false
}
