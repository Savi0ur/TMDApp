package com.haraev.authentication.di.component

import com.haraev.authentication.di.module.makePin.MakePinCodeViewModelModule
import com.haraev.authentication.presentation.makePin.MakePinCodeFragment
import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import dagger.Component

@FeatureModuleScope
@Component(
    modules = [
        MakePinCodeViewModelModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface MakePinCodeComponent {

    fun inject(target: MakePinCodeFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): MakePinCodeComponent {
                return DaggerMakePinCodeComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}