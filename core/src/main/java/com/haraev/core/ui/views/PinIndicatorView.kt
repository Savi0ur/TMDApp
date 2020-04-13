package com.haraev.core.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.haraev.core.R
import kotlinx.android.synthetic.main.view_pin_indicator.view.*

class PinIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var nextUncoloredIndicatorIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pin_indicator, this, true)
    }

    private val indicatorsList by lazy {
        listOf(
            pin_indicator_1,
            pin_indicator_2,
            pin_indicator_3,
            pin_indicator_4
        )
    }

    fun turnOnNextIndicator() {
        if (nextUncoloredIndicatorIndex == 0) {
            turnOffAllIndicators()
        }
        if (nextUncoloredIndicatorIndex <= 3) {
            indicatorsList[nextUncoloredIndicatorIndex].backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.onPrimary)
            nextUncoloredIndicatorIndex++
        }
    }

    fun turnOffLastIndicator() {
        if (nextUncoloredIndicatorIndex >= 1) {
            nextUncoloredIndicatorIndex--
            indicatorsList[nextUncoloredIndicatorIndex].backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.darkBlue)
        }
    }

    fun turnOffAllIndicators() {
        indicatorsList.forEach {
            it.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.darkBlue)
        }
        nextUncoloredIndicatorIndex = 0
    }

    fun showError() {
        indicatorsList.forEach {
            it.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.error)
        }
    }

}