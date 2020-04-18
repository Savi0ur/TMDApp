package com.haraev.main.presentation

import io.reactivex.subjects.PublishSubject

object BottomNavigationRouter {

    val router : PublishSubject<BottomNavigationScreen> = PublishSubject.create()

    fun navigateToSearchScreen() {
        router.onNext(BottomNavigationScreen.SCREEN_SEARCH)
    }

    fun navigateToFavoriteScreen() {
        router.onNext(BottomNavigationScreen.SCREEN_FAVORITE)
    }

    fun navigateToProfileScreen() {
        router.onNext(BottomNavigationScreen.SCREEN_PROFILE)
    }
}

enum class BottomNavigationScreen {
    SCREEN_SEARCH,
    SCREEN_FAVORITE,
    SCREEN_PROFILE
}

