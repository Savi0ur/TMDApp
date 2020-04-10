package com.haraev.core.ui

data class ShowMessage(val messageResId: Int) : Event

data class ShowErrorMessage(val messageResId: Int) : Event