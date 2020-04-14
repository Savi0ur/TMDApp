package com.haraev.core.di.provider

import com.haraev.core.cryptography.Cryptographer

interface SecurityProvider {

    fun getCryptographer() : Cryptographer
}