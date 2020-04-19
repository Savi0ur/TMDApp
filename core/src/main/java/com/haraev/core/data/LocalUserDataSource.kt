package com.haraev.core.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.haraev.core.cryptography.Cryptographer

class LocalUserDataSource(
    private val prefs: SharedPreferences,
    private val cryptographer: Cryptographer
) {

    var sessionId: String? = getFromSPref(KEY_SESSION_ID)
        set(value) {
            saveToSPref(KEY_SESSION_ID, value)
            field = value
        }

    var userLogin: String? = getFromSPref(KEY_USER_LOGIN)
        set(value) {
            saveToSPref(KEY_USER_LOGIN, value)
            field = value
        }

    var userPassword: String? = getFromSPref(KEY_USER_PASSWORD)
        set(value) {
            saveToSPref(KEY_USER_PASSWORD, value)
            field = value
        }

    var userPinCodeHash: String? = getFromSPref(KEY_USER_PIN_CODE_HASH)
        set(value) {
            saveToSPref(KEY_USER_PIN_CODE_HASH, value)
            field = value
        }

    var biometricAct: Boolean? = getFromSPref(KEY_BIOMETRIC_ACT)?.toBoolean()
        set(value) {
            saveToSPref(KEY_BIOMETRIC_ACT, value.toString())
            field = value
        }

    fun requireSessionId(): String {
        return sessionId ?: throw NullPointerException("sessionId = null")
    }

    private fun saveToSPref(key: String, value: String?) {
        val encryptedValue = value?.let { cryptographer.encrypt(it) }
        prefs.edit { putString(key, encryptedValue) }
    }

    private fun getFromSPref(key: String): String? {
        val encryptedValue = prefs.getString(key, null)
        return encryptedValue?.let { cryptographer.decrypt(it) }
    }

    companion object {
        private const val KEY_SESSION_ID = "KEY_SESSION_ID"
        private const val KEY_USER_LOGIN = "KEY_USER_LOGIN"
        private const val KEY_USER_PASSWORD = "KEY_USER_PASSWORD"
        private const val KEY_USER_PIN_CODE_HASH = "KEY_USER_PIN"
        private const val KEY_BIOMETRIC_ACT = "KEY_BIOMETRIC_ACT"
    }
}