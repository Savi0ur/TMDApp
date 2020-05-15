package com.haraev.tmdapp.ui

import com.haraev.core.ui.Event

sealed class MainEvents : Event {
    object OpenLoginScreen : MainEvents()

    object OpenSearchScreen : MainEvents()

    object OpenUsePinCodeScreen : MainEvents()

    class ShowDialog(val dialogMessageId: Int, val onDismissAction: () -> Unit) : MainEvents()
}