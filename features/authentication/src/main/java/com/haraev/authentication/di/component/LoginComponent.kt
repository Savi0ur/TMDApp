package com.haraev.authentication.di.component

import com.haraev.authentication.di.module.login.LoginModule
import com.haraev.authentication.di.module.login.LoginViewModelModule
import com.haraev.authentication.presentation.login.LoginFragment
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import dagger.Component

@FeatureModuleScope
@Component(
    modules = [
        LoginModule::class,
        LoginViewModelModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
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