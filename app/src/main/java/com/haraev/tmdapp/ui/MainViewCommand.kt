package com.haraev.tmdapp.ui

import com.haraev.core.aac.Event

sealed class MainEvents : Event {
    object OpenLoginScreen : MainEvents()

    object OpenSearchScreen : MainEvents()
}
