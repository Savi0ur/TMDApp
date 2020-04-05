package com.haraev.main.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import com.haraev.main.di.module.NetworkModule
import com.haraev.main.di.module.search.SearchModule
import com.haraev.main.di.module.search.SearchViewModelModule
import com.haraev.main.presentation.search.SearchFragment
import dagger.Component

@FeatureModuleScope
@Component(
    modules =
    [
        SearchModule::class,
        SearchViewModelModule::class,
        NetworkModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface SearchComponent {

    fun inject(target: SearchFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): SearchComponent {
                return DaggerSearchComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}