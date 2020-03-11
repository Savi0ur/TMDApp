package com.haraev.core.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

abstract class BaseFragment(fragmentLayoutId: Int) : Fragment(fragmentLayoutId) {

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}