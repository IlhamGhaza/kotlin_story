package com.igz.kotlin_story.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "session_prefs"

val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class SessionManager(private val context: Context) {

    private object Keys {
        val TOKEN: Preferences.Key<String> = stringPreferencesKey("token")
        val USER_ID: Preferences.Key<String> = stringPreferencesKey("user_id")
        val USER_NAME: Preferences.Key<String> = stringPreferencesKey("user_name")
        val LOGGED_IN: Preferences.Key<Boolean> = booleanPreferencesKey("logged_in")
        val THEME: Preferences.Key<String> = stringPreferencesKey("theme") // light|dark|system
        val LOCALE: Preferences.Key<String> = stringPreferencesKey("locale") // id|en
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[Keys.TOKEN] }
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { it[Keys.LOGGED_IN] ?: false }
    val userNameFlow: Flow<String?> = context.dataStore.data.map { it[Keys.USER_NAME] }
    val themeFlow: Flow<String> = context.dataStore.data.map { it[Keys.THEME] ?: "system" }
    val localeFlow: Flow<String> = context.dataStore.data.map { it[Keys.LOCALE] ?: "id" }

    suspend fun saveLogin(token: String, userId: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_NAME] = name
            prefs[Keys.LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.TOKEN)
            prefs.remove(Keys.USER_ID)
            prefs.remove(Keys.USER_NAME)
            prefs[Keys.LOGGED_IN] = false
        }
    }

    suspend fun setTheme(mode: String) { // light|dark|system
        context.dataStore.edit { it[Keys.THEME] = mode }
    }

    suspend fun setLocale(code: String) { // id|en
        context.dataStore.edit { it[Keys.LOCALE] = code }
    }
}
