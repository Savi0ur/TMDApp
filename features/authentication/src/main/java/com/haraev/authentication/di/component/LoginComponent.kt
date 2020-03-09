package com.haraev.authentication.di.component

import com.haraev.authentication.di.module.LoginModule
import com.haraev.authentication.presentation.LoginFragment
import dagger.Component

@Component(modules = [LoginModule::class])
interface LoginComponent {

    fun inject(target: LoginFragment)

    class Builder private constructor() {

        companion object {

            fun build(): LoginComponent {
                return DaggerLoginComponent.builder()
                    .build()
            }
        }
    }
}