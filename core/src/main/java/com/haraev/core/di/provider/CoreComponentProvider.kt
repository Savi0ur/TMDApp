package com.haraev.core.di.provider

import com.haraev.core.di.component.CoreComponent

interface CoreComponentProvider {

    fun getCoreComponent() : CoreComponent
}