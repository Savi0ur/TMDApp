package com.haraev.main.presentation.main

import com.haraev.core.ui.Event

sealed class MainFeatureEvents : Event {

    object NavigateToSearchScreen : MainFeatureEvents()
    object NavigateToFavoriteScreen : MainFeatureEvents()
    object NavigateToProfileScreen : MainFeatureEvents()

}