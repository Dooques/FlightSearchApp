package com.example.flightsearchapp.ui.home

import android.content.res.Resources
import android.text.Layout
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.room.Airport
import com.example.flightsearchapp.data.room.Favorite
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

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
        val highlighted = true
        Log.d("", "SFList: ${airportList.airports}")

        Surface(modifier = Modifier.padding(it)) {
            Column {
                SearchField(viewModel = viewModel)
                if (searchTerm.search.isNotEmpty()) {
                    if (viewModel.isAirportSelected && selectedAirport?.airport != null) {
                        AirportElement(
                            airport = selectedAirport?.airport!!,
                            highlighted = highlighted,
                            viewModel = viewModel
                        )
                    }
                    SearchResults(
                        searchResults = airportList.airports,
                        viewModel = viewModel,
                    )
                } else {
                    FavoritesList(
                        favoritesList = favoritesList.favorites,
                        viewModel = viewModel
                    )
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
    val searchTerm by viewModel.searchUiState.collectAsState()
    TextField(
        value = searchTerm.search,
        onValueChange = {
            viewModel.setSearchTerms(it)
            Log.d("", "SearchTerm: ${viewModel.searchUiState.value}")
        },
        maxLines = 1,
        placeholder = { Text("Search for an airport...") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear Search",
                modifier = modifier.clickable {
                    viewModel.setSearchTerms("")
                    viewModel.deselectAirport()
                }
            ) },
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun AirportElement(
    modifier: Modifier = Modifier,
    airport: Airport,
    highlighted: Boolean,
    viewModel: FlightSearchViewModel
) {
    val color =
        if (highlighted)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.background
    val padding =
        if (highlighted)
            modifier.padding(start = 64.dp, end = 32.dp)
        else
            modifier.padding(start = 64.dp, end = 16.dp)
    val airportSelected = viewModel.isAirportSelected
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(color)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp)
                .clickable {
                    viewModel.airportClicked(airport)
                }
        ) {
            Text(
                airport.iataCode, fontWeight = FontWeight.Bold
            )
            Text(
                text = airport.name,
                modifier = padding
            )
            if (highlighted) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Deselect Airport",
                    modifier = modifier
                        .align(alignment = Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .clickable { viewModel.deselectAirport() }
                )
            }
        }
        if (airportSelected && !highlighted) {
            Text(
                text = "Click to add as a favorite route",
                fontSize = 10.sp,
                modifier = modifier.padding(start = 80.dp)
            )
            HorizontalDivider()
        } else {
            HorizontalDivider(modifier = modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    searchResults: List<Airport>,
    viewModel: FlightSearchViewModel,
) {
    val highlighted = false
    Log.d("", "AirportList: $searchResults")
    LazyColumn {
        items(
            items = searchResults,
            key = { item -> item.id }
        ) { airport ->
            // Airport Result
           AirportElement(
               airport = airport,
               highlighted = highlighted,
               viewModel = viewModel
           )
        }
    }
}

@Composable
fun FavoritesList(
    modifier: Modifier = Modifier,
    favoritesList: List<Favorite>,
    viewModel: FlightSearchViewModel
) {
    Column(
        modifier.background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(start = 16.dp, top = 16.dp)
        ) {
            Text("Your Favourite Routes")
        }
        HorizontalDivider(modifier = modifier.padding(top = 16.dp))
    }
    LazyColumn {
        items(
            items = favoritesList,
            key = { favorite -> favorite.id }
        ) { item ->
            // Favorite
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(start = 16.dp, top = 16.dp)
            ) {
                Text(item.departureCode)
                Text(" to " + item.destinationCode)
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Favorite Route",
                        Modifier.clickable { viewModel.deleteFavorite(item) }
                    )
                }
            }
            HorizontalDivider(modifier = modifier.padding(top = 16.dp))
        }
    }
}
@Composable
@Preview("SearchScreenPreview")
fun SearchScreenPreview() {
    FlightSearchAppTheme {
        HomeScreen()
    }
}