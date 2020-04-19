package com.haraev.authentication.di.component

import com.haraev.authentication.di.module.usePin.UsePinCodeModule
import com.haraev.authentication.di.module.usePin.UsePinCodeViewModelModule
import com.haraev.authentication.presentation.pinCode.usePin.UsePinCodeFragment
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import dagger.Component

@FeatureModuleScope
@Component(
    modules = [
        UsePinCodeViewModelModule::class,
        UsePinCodeModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface UsePinCodeComponent {

    fun inject(target: UsePinCodeFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): UsePinCodeComponent {
                return DaggerUsePinCodeComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}