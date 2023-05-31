package com.example.languagegym.helpers

import android.view.MotionEvent
import android.view.View

class UniversalTouchListener : View.OnTouchListener {
    private var clickStartTime: Long = 0
    private var longClickDuration: Long = 500
    private var clickListener: OnClickListener? = null
    private var longClickListener: OnLongClickListener? = null

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                clickStartTime = System.currentTimeMillis()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val clickDuration = System.currentTimeMillis() - clickStartTime
                if (clickDuration < longClickDuration) {
                    clickListener?.onClick(view)
                } else {
                    longClickListener?.onLongClick(view)
                }
                return true
            }
        }
        return false
    }

    fun setOnClickListener(listener: OnClickListener) {
        clickListener = listener
    }

    fun setOnLongClickListener(listener: OnLongClickListener) {
        longClickListener = listener
    }

    interface OnClickListener {
        fun onClick(view: View)
    }

    interface OnLongClickListener {
        fun onLongClick(view: View)
    }
}