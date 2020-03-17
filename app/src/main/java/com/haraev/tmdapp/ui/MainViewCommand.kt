package com.haraev.tmdapp.ui

sealed class MainViewCommand {
    object OpenLoginScreen : MainViewCommand()

    object OpenSearchScreen : MainViewCommand()
}