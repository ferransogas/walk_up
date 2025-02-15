package com.github.ferransogas.walk_up.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "alarm_data_store")

object AlarmDataStore {
    private val ALARM_HOUR = intPreferencesKey("alarm_hour")
    private val ALARM_MINUTE = intPreferencesKey("alarm_minute")
    private val ALARM_ENABLED = booleanPreferencesKey("alarm_enabled")
    private val ALARM_FOREGROUND_ENABLED = booleanPreferencesKey("alarm_foreground_enabled")

    suspend fun saveAlarm(context: Context, hour: Int, minute: Int, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ALARM_HOUR] = hour
            preferences[ALARM_MINUTE] = minute
            preferences[ALARM_ENABLED] = enabled
        }
    }

    suspend fun toggleAlarm(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ALARM_ENABLED] = enabled
        }
    }

    suspend fun setForegroundEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ALARM_FOREGROUND_ENABLED] = enabled
        }
    }

    fun getAlarm(context: Context): Flow<AlarmData> {
        return context.dataStore.data.map { preferences ->
            AlarmData(
                preferences[ALARM_HOUR] ?: 0,
                preferences[ALARM_MINUTE] ?: 0,
                preferences[ALARM_ENABLED] ?: false
            )
        }
    }

    fun getForegroundEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ALARM_FOREGROUND_ENABLED] ?: false
        }
    }
}

data class AlarmData(val hour: Int, val minute: Int, val enabled: Boolean)
