package com.haraev.main.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import com.haraev.main.di.module.DataModule
import com.haraev.main.di.module.NetworkModule
import com.haraev.main.di.module.favorite.FavoriteModule
import com.haraev.main.di.module.favorite.FavoriteViewModelModule
import com.haraev.main.di.module.search.SearchModule
import com.haraev.main.presentation.favorite.FavoriteFragment
import dagger.Component

@FeatureModuleScope
@Component(
    modules =
    [
        FavoriteViewModelModule::class,
        FavoriteModule::class,
        SearchModule::class,
        DataModule::class,
        NetworkModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface FavoriteComponent {

    fun inject(target: FavoriteFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): FavoriteComponent {
                return DaggerFavoriteComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}