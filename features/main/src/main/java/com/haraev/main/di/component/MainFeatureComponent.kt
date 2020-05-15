package com.haraev.main.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import com.haraev.main.di.module.DataModule
import com.haraev.main.di.module.main.MainFeatureViewModelModule
import com.haraev.main.presentation.main.MainFeatureHostFragment
import dagger.Component

@FeatureModuleScope
@Component(
    modules =
    [
        MainFeatureViewModelModule::class,
        DataModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface MainFeatureComponent {

    fun inject(target: MainFeatureHostFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): MainFeatureComponent {
                return DaggerMainFeatureComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}