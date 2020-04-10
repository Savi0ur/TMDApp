package com.haraev.main.presentation.profile

import com.haraev.core.ui.Event

sealed class ProfileEvents : Event {
    object Logout : ProfileEvents()
}