package com.example.flightsearchapp.repository

import android.content.Context
import com.example.flightsearchapp.data.room.AirportDatabase

interface AppContainer {
    val airportRepository: AirportRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val airportRepository by lazy {
        OfflineRepository(
            airportDao = AirportDatabase.getDatabase(context).airportDao(),
        )
    }
}