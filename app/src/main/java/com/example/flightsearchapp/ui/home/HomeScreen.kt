package com.example.flightsearchapp.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.data.room.Airport
import com.example.flightsearchapp.data.room.Favorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Flight Search") }
            )
        }
    ) {
        val viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
        val searchTerm by viewModel.searchUiState.collectAsState()
        val airportList by viewModel.airportsUiState.collectAsState()
        val favoritesList by viewModel.getFavorites().collectAsState()
        val selectedAirport by viewModel.selectedAirportState.collectAsState()
        Log.d("", "SFList: ${airportList.airports}")

        Surface(modifier = Modifier.padding(it)) {
            Column {
                SearchField(viewModel = viewModel)
                if (searchTerm.search.isNotEmpty()) {
                    if (viewModel.isAirportSelected && selectedAirport?.airport != null) {
                        SelectedAirport(selectedAirport = selectedAirport?.airport!!)
                    }
                    SearchResults(
                        searchResults = airportList.airports,
                        viewModel = viewModel,
                    )
                } else {
                    FavoritesList(favoritesList.favorites)
                }
            }
        }
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel
) {
    var searchValue by remember { mutableStateOf("") }
    TextField(
        value = searchValue,
        onValueChange = {
            searchValue = it
            viewModel.setSearchTerms(searchValue)
            Log.d("", "SearchTerm: ${viewModel.searchUiState.value}")
        },
        maxLines = 1,
        placeholder = { Text("Search for an airport...") },
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun SelectedAirport(
    modifier: Modifier = Modifier,
    selectedAirport: Airport
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(Color.Gray.copy(alpha = 0.25f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(start = 16.dp, bottom = 16.dp, top = 16.dp)

        ) {
            Text(selectedAirport.iataCode, fontWeight = FontWeight.Bold)
            Spacer(modifier.size(height = 0.dp, width = 16.dp))
            Text(selectedAirport.name)
        }
        HorizontalDivider()
    }
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    searchResults: List<Airport>,
    viewModel: FlightSearchViewModel,
) {
    Log.d("", "AirportList: $searchResults")
    LazyColumn {
        items(
            items = searchResults,
            key = { item -> item.id }
        ) { item ->
            // Airport Result
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .clickable {
                        viewModel.selectAirport(item)
                    }
            ) {
                Text(item.iataCode, fontWeight = FontWeight.Bold)
                Spacer(modifier.size(height = 0.dp, width = 16.dp))
                Text(item.name)
            }
            HorizontalDivider(modifier = modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun FavoritesList(
    favoritesList: List<Favorite>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(
            items = favoritesList,
            key = { item -> item.id }
        ) { item ->
            // Favorite
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(start = 16.dp, top = 16.dp)
            ) {
                Text(item.departureCode)
                Text(" to " + item.destinationCode)
            }
            HorizontalDivider(modifier = modifier.padding(top = 16.dp))
        }
    }
}
@Composable
@Preview("SearchScreenPreview")
fun SearchScreenPreview() {
    HomeScreen()
}