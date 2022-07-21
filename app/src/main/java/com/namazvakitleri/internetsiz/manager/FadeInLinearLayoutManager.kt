package com.namazvakitleri.internetsiz.manager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager

open class FadeInLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val enterInterpolator = AnticipateOvershootInterpolator(2f)

    override fun addView(child: View, index: Int) {
        super.addView(child, index)
        val h = 70f

        child.translationY = if(index == 0){
            -h
        } else{
            h
        }

        child.alpha = 0.3f
        child.animate().translationY(0f).alpha(1f)
            .setInterpolator(enterInterpolator)
            .setDuration(500L)
    }
}