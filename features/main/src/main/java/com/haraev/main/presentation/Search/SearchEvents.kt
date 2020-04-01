package com.haraev.main.presentation.search

import com.haraev.core.aac.Event

sealed class SearchEvents : Event {
    data class ErrorMessage(val messageResId: Int) : SearchEvents()
}