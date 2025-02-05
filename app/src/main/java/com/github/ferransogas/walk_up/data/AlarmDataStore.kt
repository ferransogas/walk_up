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

    suspend fun saveAlarm(context: Context, hour: Int, minutes: Int, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ALARM_HOUR] = hour
            preferences[ALARM_MINUTE] = minutes
            preferences[ALARM_ENABLED] = enabled
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
}

data class AlarmData(val hour: Int, val minutes: Int, val enabled: Boolean)
