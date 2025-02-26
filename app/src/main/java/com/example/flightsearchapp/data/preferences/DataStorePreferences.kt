package com.example.flightsearchapp.data.preferences

import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
){
    private  companion object {
        val LAST_SEARCH = stringPreferencesKey("last_search")
    }

    suspend fun saveLastSearch(lastSearch: String) {
        dataStore.edit { preferences ->
            preferences[LAST_SEARCH] = lastSearch
        }
    }

    val lastSearch: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
            else {
                throw it
            }
        }
        .map { preferences ->
        preferences[LAST_SEARCH] ?: ""
    }
}