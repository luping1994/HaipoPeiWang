package net.suntrans.haipopeiwang.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.FloatingActionButton.Behavior
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator

/**
 * Created by Looney on 2018/8/15.
 * Des:
 */
class BehaviorDefault : Behavior {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    val FAST_OUT_LINEAR_IN_INTERPOLATOR: Interpolator = FastOutLinearInInterpolator()


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {

        var msg = "";
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            msg = "ViewCompat.SCROLL_AXIS_VERTICAL"
        } else if (axes == ViewCompat.SCROLL_AXIS_HORIZONTAL) {
            msg = "ViewCompat.SCROLL_AXIS_HORIZONTAL"

        } else {
            msg = "ViewCompat.SCROLL_AXIS_NONE"

        }

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            startHideAnimation(child)
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            startShowAnimation(child)
        }
    }

    fun startHideAnimation(view: View) {
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(FAST_OUT_LINEAR_IN_INTERPOLATOR)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.INVISIBLE
                    }
                })
                .start()
    }

    fun startShowAnimation(view: View) {
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(FAST_OUT_LINEAR_IN_INTERPOLATOR)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        view.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.VISIBLE
                    }
                })
                .start()
    }
}
