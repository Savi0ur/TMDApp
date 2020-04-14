package com.haraev.tmdapp

import android.app.Application
import com.haraev.core.cryptography.Cryptographer
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.provider.CoreComponentProvider
import com.haraev.core.di.provider.SecurityProvider
import timber.log.Timber

class TmdAppApplication : Application(), CoreComponentProvider, SecurityProvider {

    private lateinit var coreComponent: CoreComponent

    private lateinit var cryptographer: Cryptographer

    override fun onCreate() {
        super.onCreate()

        buildDi()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        cryptographer = Cryptographer(applicationContext)
        cryptographer.register()
    }

    private fun buildDi() {
        coreComponent = CoreComponent.Builder.build(this, "DEFAULT_APP_PREF")
    }

    override fun getCoreComponent(): CoreComponent = coreComponent

    override fun getCryptographer(): Cryptographer = cryptographer
}