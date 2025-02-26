package com.example.flightsearchapp.repository

import android.util.Log
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.AirportDao
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.ui.home.AirportsUiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface AirportRepository {
    fun getAirports(name: String): Flow<List<Airport>>

    suspend fun insert(favorite: Favorite)

    suspend fun delete(departingAirport: String, destination: String)

    fun getFavorites(): Flow<List<Favorite>>
}

class OfflineRepository(
    private val airportDao: AirportDao,
): AirportRepository {
    override fun getAirports(name: String): Flow<List<Airport>> =
        airportDao.getAirports(name)

    override fun getFavorites(): Flow<List<Favorite>> =
        airportDao.getFavorites()

    override suspend fun insert(favorite: Favorite) =
        airportDao.insert(favorite)

    override suspend fun delete(departingAirport: String, destination: String) =
        airportDao.delete(departingAirport, destination)
}