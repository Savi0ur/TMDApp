package com.haraev.core.snackbar

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.haraev.core.R

object SnackbarFactory {

    fun create(
        mainView: View,
        messageText: String,
        containerResId: Int,
        duration: Int,
        @ColorRes backgroundColor: Int,
        @ColorRes textColor: Int
    ): Snackbar? {

        val viewGroup = mainView.findViewById(containerResId) as? ViewGroup

        return viewGroup?.let {
            Snackbar
                .make(viewGroup, messageText, duration)
                .decorate(backgroundColor, textColor)
        }
    }

    private fun Snackbar.decorate(@ColorRes backgroundId: Int, @ColorRes textColorId: Int): Snackbar {
        val layout = view as? Snackbar.SnackbarLayout ?: return this

        val textView = with(layout) {
            backgroundTintList = ContextCompat.getColorStateList(view.context, backgroundId)
            findViewById<TextView>(R.id.snackbar_text)
        }

        textView.setTextColor(ContextCompat.getColor(view.context, textColorId))

        return this
    }
}