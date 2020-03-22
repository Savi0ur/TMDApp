package com.haraev.core.data

import android.content.SharedPreferences
import androidx.core.content.edit

class SessionLocalDataSource(private val prefs: SharedPreferences) {

    var sessionId: String? = prefs.getString(KEY_SESSION_ID, null)
        set(value) {
            prefs.edit { putString(KEY_SESSION_ID, value) }
            field = value
        }

    var userLogin: String? = prefs.getString(KEY_USER_LOGIN, null)
        set(value) {
            prefs.edit { putString(KEY_USER_LOGIN, value) }
            field = value
        }

    var userPassword: String? = prefs.getString(KEY_USER_PASSWORD, null)
        set(value) {
            prefs.edit { putString(KEY_USER_PASSWORD, value) }
            field = value
        }

    companion object {
        private const val KEY_SESSION_ID = "KEY_SESSION_ID"
        private const val KEY_USER_LOGIN = "KEY_USER_LOGIN"
        private const val KEY_USER_PASSWORD = "KEY_USER_PASSWORD"
    }
}