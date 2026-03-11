package com.example.progresstracker.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartTimePreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKey {
        val START_TIME = longPreferencesKey("start_time")
    }

    fun readStartTime(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.START_TIME] ?: 0L
        }
    }

    suspend fun writeStartTime(startTime: Long) {
        dataStore.edit { it[PreferencesKey.START_TIME]=startTime }
    }

}