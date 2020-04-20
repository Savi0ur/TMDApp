package com.haraev.core.cryptography

import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.config.TinkConfig
import okio.internal.commonAsUtf8ToByteArray
import okio.internal.commonToUtf8String

class Cryptographer(
    private val keysetHandle: KeysetHandle
) {

    private var aead: Aead? = null

    fun register() {
        aead = keysetHandle.getPrimitive(Aead::class.java)
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
}