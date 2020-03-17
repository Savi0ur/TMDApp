package com.haraev.tmdapp

import android.app.Application
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.provider.CoreComponentProvider
import timber.log.Timber

class TmdAppApplication : Application(), CoreComponentProvider {

    private lateinit var coreComponent : CoreComponent

    override fun onCreate() {
        super.onCreate()

        buildDi()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun buildDi() {
        coreComponent = CoreComponent.Builder.build(this, "DEFAULT_APP_PREF")
    }

    override fun getCoreComponent() : CoreComponent = coreComponent

}