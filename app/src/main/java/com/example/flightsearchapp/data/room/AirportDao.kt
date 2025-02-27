package com.example.flightsearchapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport WHERE name LIKE '%' || :name || '%' OR iata_code LIKE '%' || :name || '%' ORDER BY passengers DESC")
    fun getAirports(name: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE NOT name = :name ORDER BY passengers DESC")
    fun getDestinations(name: String): Flow<List<Airport>>

    @Insert
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorite " +
            "WHERE departure_code = :departure " +
            "AND destination_code = :destination")
    suspend fun delete(departure: String, destination: String)

    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<List<Favorite>>

}