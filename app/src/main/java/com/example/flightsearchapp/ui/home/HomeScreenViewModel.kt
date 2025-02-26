package com.example.flightsearchapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.flightSearchApplication
import com.example.flightsearchapp.repository.AirportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val airportRepository: AirportRepository
): ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FlightSearchViewModel(
                    flightSearchApplication().container.airportRepository
                )
            }
        }
        const val TIME_MILLIS = 5_000L
    }

    private val _searchUiState = MutableStateFlow(SearchUiState(""))

    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    fun setSearchTerms(search: String) {
        _searchUiState.update { currentState ->
            currentState.copy(search = search)
        }
        updateAirportList()
    }

    private val _airportsUiState = MutableStateFlow(AirportsUiState(listOf()))

    val airportsUiState: StateFlow<AirportsUiState> = _airportsUiState.asStateFlow()

    init {
        updateAirportList()
    }

    private fun updateAirportList() {
        viewModelScope.launch {
            searchUiState.collect { searchUiState ->
                airportRepository.getAirports(searchUiState.search).map {
                    AirportsUiState(it)
                }.collect {
                    _airportsUiState.value = it
                }

            }
        }
    }


    fun getFavorites() = airportRepository.getFavorites().map { FavoritesUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FavoritesUiState()
        )
}

data class SearchUiState(
    val search: String
)

data class AirportsUiState(
    val airports: List<Airport> = listOf()
)

data class FavoritesUiState(
    val favorites: List<Favorite> = listOf()
)

