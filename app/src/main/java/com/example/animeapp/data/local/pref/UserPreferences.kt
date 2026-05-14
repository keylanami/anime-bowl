package com.example.animeapp.data.local.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    private val IS_GRID_MODE = booleanPreferencesKey("is_grid_mode")

    val isGridMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_GRID_MODE] ?: false
    }

    suspend fun toggleGridMode() {
        context.dataStore.edit { preferences ->
            val current = preferences[IS_GRID_MODE] ?: false
            preferences[IS_GRID_MODE] = !current
        }
    }
}