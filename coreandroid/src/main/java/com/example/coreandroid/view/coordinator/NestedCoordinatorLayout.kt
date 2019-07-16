package com.example.coreandroid.view.coordinator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper

class NestedCoordinatorLayout : androidx.coordinatorlayout.widget.CoordinatorLayout, NestedScrollingChild {

    private val mChildHelper = NestedScrollingChildHelper(this)

    constructor(context: Context) : super(context) {
        isNestedScrollingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        isNestedScrollingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isNestedScrollingEnabled = true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        val tConsumed = Array(2) { IntArray(2) }
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        dispatchNestedPreScroll(dx, dy, tConsumed[1], null)
        consumed[0] = tConsumed[0][0] + tConsumed[1][0]
        consumed[1] = tConsumed[0][1] + tConsumed[1][1]
    }

    override fun onNestedScroll(
            target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        /* Disable the scrolling behavior of our own children */
        super.onStopNestedScroll(target, type)
        /* Disable the scrolling behavior of the parent's other children  */
        stopNestedScroll()
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        /* Enable the scrolling behavior of our own children */
        val tHandled = super.onStartNestedScroll(child, target, nestedScrollAxes, type)
        /* Enable the scrolling behavior of the parent's other children  */
        return startNestedScroll(nestedScrollAxes) || tHandled
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        /* Enable the scrolling behavior of our own children */
        val tHandled = super.onStartNestedScroll(child, target, nestedScrollAxes)
        /* Enable the scrolling behavior of the parent's other children  */
        return startNestedScroll(nestedScrollAxes) || tHandled
    }

    override fun onStopNestedScroll(target: View) {
        /* Disable the scrolling behavior of our own children */
        super.onStopNestedScroll(target)
        /* Disable the scrolling behavior of the parent's other children  */
        stopNestedScroll()
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val tConsumed = Array(2) { IntArray(2) }
        super.onNestedPreScroll(target, dx, dy, tConsumed[0])
        dispatchNestedPreScroll(dx, dy, tConsumed[1], null)
        consumed[0] = tConsumed[0][0] + tConsumed[1][0]
        consumed[1] = tConsumed[0][1] + tConsumed[1][1]
    }

    override fun onNestedScroll(
            target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        val tHandled = super.onNestedPreFling(target, velocityX, velocityY)
        return dispatchNestedPreFling(velocityX, velocityY) || tHandled
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        val tHandled = super.onNestedFling(target, velocityX, velocityY, consumed)
        return dispatchNestedFling(velocityX, velocityY, consumed) || tHandled
    }

    override fun isNestedScrollingEnabled(): Boolean = mChildHelper.isNestedScrollingEnabled

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun startNestedScroll(axes: Int): Boolean = mChildHelper.startNestedScroll(axes)

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean = mChildHelper.hasNestedScrollingParent()

    override fun dispatchNestedScroll(
            dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?
    ): Boolean = mChildHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, offsetInWindow
    )

    override fun dispatchNestedPreScroll(
            dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?
    ): Boolean = mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)

    override fun dispatchNestedFling(
            velocityX: Float, velocityY: Float, consumed: Boolean
    ): Boolean = mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)

    override fun dispatchNestedPreFling(
            velocityX: Float, velocityY: Float
    ): Boolean = mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
}