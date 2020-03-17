package com.haraev.tmdapp.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.AppModuleScope
import com.haraev.tmdapp.di.module.MainModule
import com.haraev.tmdapp.ui.MainActivity
import dagger.Component

@AppModuleScope
@Component(modules = [MainModule::class], dependencies = [CoreComponent::class])
interface MainComponent {

    fun inject(target: MainActivity)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): MainComponent {
                return DaggerMainComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}