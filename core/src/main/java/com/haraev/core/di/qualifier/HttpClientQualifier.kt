package com.haraev.core.di.qualifier

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class HttpClientQualifier(
    val withAuthenticator : Boolean
)