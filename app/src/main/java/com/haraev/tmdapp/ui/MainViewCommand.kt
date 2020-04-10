package com.haraev.tmdapp.ui

import com.haraev.core.ui.Event

sealed class MainEvents : Event {
    object OpenLoginScreen : MainEvents()

    object OpenSearchScreen : MainEvents()
}
