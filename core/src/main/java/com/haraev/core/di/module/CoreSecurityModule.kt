package com.haraev.core.di.module

import android.content.Context
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.haraev.core.cryptography.Cryptographer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreSecurityModule {

    @Provides
    @Singleton
    fun provideKeysetHandle(context: Context): KeysetHandle {
        return AndroidKeysetManager.Builder()
            .withSharedPref(
                context,
                TINK_KEYSET_NAME,
                PREF_FILE_NAME
            )
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
    }

    @Provides
    @Singleton
    fun provideCryptographer(keysetHandle: KeysetHandle): Cryptographer {
        val cryptographer = Cryptographer(keysetHandle)
        cryptographer.register()
        return cryptographer
    }

    companion object {
        private const val PREF_FILE_NAME = "tmdb_pref"
        private const val TINK_KEYSET_NAME = "tmdb_keyset"
        private const val MASTER_KEY_URI = "android-keystore://tmdb_master_key"
    }
}