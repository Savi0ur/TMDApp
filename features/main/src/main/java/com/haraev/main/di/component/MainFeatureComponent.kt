package com.haraev.main.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import com.haraev.main.di.module.NetworkModule
import com.haraev.main.di.module.ProfileModule
import com.haraev.main.di.module.SearchModule
import com.haraev.main.presentation.profile.ProfileFragment
import com.haraev.main.presentation.search.SearchFragment
import dagger.Component

@FeatureModuleScope
@Component(
    modules =
    [
        ProfileModule::class,
        SearchModule::class,
        NetworkModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface MainFeatureComponent {

    fun inject(target: ProfileFragment)

    fun inject(target: SearchFragment)

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