package com.haraev.authentication.di.component

import com.haraev.authentication.di.module.repeatPin.RepeatPinCodeModule
import com.haraev.authentication.di.module.repeatPin.RepeatPinCodeViewModelModule
import com.haraev.authentication.presentation.repeatPin.RepeatPinCodeFragment
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import dagger.Component

@FeatureModuleScope
@Component(
    modules = [
        RepeatPinCodeViewModelModule::class,
        RepeatPinCodeModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface RepeatPinCodeComponent {

    fun inject(target: RepeatPinCodeFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): RepeatPinCodeComponent {
                return DaggerRepeatPinCodeComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}