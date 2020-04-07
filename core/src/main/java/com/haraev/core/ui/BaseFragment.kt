package com.haraev.core.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.haraev.core.R
import com.haraev.core.snackbar.SnackbarFactory

abstract class BaseFragment(fragmentLayoutId: Int) : Fragment(fragmentLayoutId) {

    private val decorView by lazy { requireActivity().window.decorView }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun showMessage(messageResId: Int, anchorView: View?) {
        val backgroundColorId = R.color.onBackgroundVariant
        val textColorId = R.color.secondary
        showSnackBar(messageResId, android.R.id.content, anchorView?.id, Snackbar.LENGTH_LONG, backgroundColorId, textColorId)
    }

    fun showErrorMessage(messageResId: Int, anchorView: View?) {
        val backgroundColorId = R.color.onBackgroundVariant
        val textColorId = R.color.error
        showSnackBar(messageResId, android.R.id.content, anchorView?.id, Snackbar.LENGTH_LONG, backgroundColorId, textColorId)
    }

    private fun showSnackBar(
        messageResId: Int,
        containerResId: Int,
        anchorViewId: Int?,
        duration: Int,
        @ColorRes backgroundColor: Int,
        @ColorRes textColor: Int
    ) {
        val message = getString(messageResId)
        val snackbar = SnackbarFactory.create(
            mainView = decorView,
            messageText = message,
            containerResId = containerResId,
            duration = duration,
            backgroundColor = backgroundColor,
            textColor = textColor
        )
        anchorViewId?.let { snackbar?.setAnchorView(it) }
        snackbar?.show()
    }
}