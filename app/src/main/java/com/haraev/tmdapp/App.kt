package com.haraev.tmdapp

import android.app.Application
import com.haraev.authentication.di.component.DaggerLoginComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        buildDi()
    }

    private fun buildDi() {
        DaggerLoginComponent.builder().build()
    }
}