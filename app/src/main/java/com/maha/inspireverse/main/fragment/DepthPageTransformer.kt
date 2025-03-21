package com.maha.inspireverse.main.fragment

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < -1 -> { // Page is off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // Moving to the left page.
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // Moving to the right page.
                    alpha = 1 - position
                    translationX = width * -position
                    scaleX = 0.75f + (0.25f * (1 - Math.abs(position)))
                    scaleY = 0.75f + (0.25f * (1 - Math.abs(position)))
                }
                else -> { // Page is off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}
