package com.haraev.authentication.di.component

import com.haraev.authentication.di.module.LoginModule
import com.haraev.authentication.presentation.LoginFragment
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import dagger.Component

@FeatureModuleScope
@Component(modules = [LoginModule::class], dependencies = [CoreComponent::class])
interface LoginComponent {

    fun inject(target: LoginFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): LoginComponent {
                return DaggerLoginComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}