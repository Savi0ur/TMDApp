package com.haraev.main.presentation.profile

import com.haraev.core.aac.Event

sealed class ProfileEvents : Event{
    object Logout : ProfileEvents()
    data class ErrorMessage(val messageResId: Int) : ProfileEvents()
}