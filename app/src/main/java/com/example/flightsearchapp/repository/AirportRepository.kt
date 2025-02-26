package com.example.flightsearchapp.repository

import com.example.flightsearchapp.data.room.Airport
import com.example.flightsearchapp.data.room.AirportDao
import com.example.flightsearchapp.data.room.Favorite
import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAirports(name: String): Flow<List<Airport>>

    fun getDestinations(name: String): Flow<List<Airport>>

    suspend fun insert(favorite: Favorite)

    suspend fun delete(departingAirport: String, destination: String)

    fun getFavorites(): Flow<List<Favorite>>
}

class OfflineRepository(
    private val airportDao: AirportDao,
): AirportRepository {
    override fun getAirports(name: String): Flow<List<Airport>> =
        airportDao.getAirports(name)

    override fun getDestinations(name: String): Flow<List<Airport>> =
        airportDao.getDestinations(name)

    override fun getFavorites(): Flow<List<Favorite>> =
        airportDao.getFavorites()

    override suspend fun insert(favorite: Favorite) =
        airportDao.insert(favorite)

    override suspend fun delete(departingAirport: String, destination: String) =
        airportDao.delete(departingAirport, destination)
}