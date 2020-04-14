package com.haraev.core.cryptography

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import okio.internal.commonAsUtf8ToByteArray
import okio.internal.commonToUtf8String

class Cryptographer(
    private val context: Context
) {

    private var aead: Aead? = null

    fun register() {
        TinkConfig.register()
        aead = getOrGenerateNewKeysetHandle().getPrimitive(Aead::class.java)
    }

    fun encrypt(text: String): String {
        val aead : Aead = this.aead ?: throw IllegalStateException("Cryptographer not registered")

        val plainText = text.commonAsUtf8ToByteArray()
        val cipherText = aead.encrypt(plainText, byteArrayOf())
        return Base64.encodeToString(cipherText, Base64.DEFAULT)
    }

    fun decrypt(text: String): String {
        val aead : Aead = this.aead ?: throw IllegalStateException("Cryptographer not registered")

        val cipherText = Base64.decode(text, Base64.DEFAULT)
        val plainText = aead.decrypt(cipherText, byteArrayOf())
        return plainText.commonToUtf8String()
    }

    private fun getOrGenerateNewKeysetHandle(): KeysetHandle {
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

    companion object {
        private const val PREF_FILE_NAME = "tmdb_pref"
        private const val TINK_KEYSET_NAME = "tmdb_keyset"
        private const val MASTER_KEY_URI = "android-keystore://tmdb_master_key"
    }

}