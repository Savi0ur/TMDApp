package com.haraev.main.di.component

import com.haraev.core.di.component.CoreComponent
import com.haraev.core.di.scope.FeatureModuleScope
import com.haraev.main.di.module.NetworkModule
import com.haraev.main.di.module.favorite.FavoriteModule
import com.haraev.main.di.module.movieDetails.MovieDetailsViewModelModule
import com.haraev.main.presentation.moviedetails.MovieDetailsFragment
import dagger.Component

@FeatureModuleScope
@Component(
    modules =
    [
        FavoriteModule::class,
        MovieDetailsViewModelModule::class,
        NetworkModule::class
    ],
    dependencies =
    [CoreComponent::class]
)
interface MovieDetailsComponent {

    fun inject(target: MovieDetailsFragment)

    class Builder private constructor() {

        companion object {

            fun build(coreComponent: CoreComponent): MovieDetailsComponent {
                return DaggerMovieDetailsComponent.builder()
                    .coreComponent(coreComponent)
                    .build()
            }
        }
    }
}