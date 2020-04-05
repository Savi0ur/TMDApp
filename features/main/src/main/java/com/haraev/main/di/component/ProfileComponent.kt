package com.haraev.main.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import com.haraev.main.di.module.NetworkModule
import com.haraev.main.di.module.profile.ProfileModule
import com.haraev.main.di.module.profile.ProfileViewModelModule
import com.haraev.main.presentation.profile.ProfileFragment
import dagger.Component

@FeatureModuleScope
@Component(
    modules =
    [
        ProfileModule::class,
        ProfileViewModelModule::class,
        NetworkModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface ProfileComponent {

    fun inject(target: ProfileFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): ProfileComponent {
                return DaggerProfileComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}