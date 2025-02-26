package com.example.flightsearchapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.flightsearchapp.data.preferences.PreferencesRepository
import com.example.flightsearchapp.repository.AppContainer
import com.example.flightsearchapp.repository.AppDataContainer

private const val LAST_SEARCH = "last_search"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAST_SEARCH
)

class FlightSearchApplication: Application() {
    lateinit var container: AppContainer
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        preferencesRepository = PreferencesRepository(dataStore)

    }
}

fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[APPLICATION_KEY] as FlightSearchApplication)