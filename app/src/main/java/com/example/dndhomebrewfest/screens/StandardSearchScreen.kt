package com.example.dndhomebrewfest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dndhomebrewfest.R
import com.example.dndhomebrewfest.Screens
import com.example.dndhomebrewfest.data.DataSource
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import java.util.Locale
import java.util.Locale.getDefault

@Composable
fun StandardSearchScreen(navController : NavController, modifier : Modifier = Modifier) {
    // lazycolumn with results

    val hbfVM : HBFViewModel = viewModel()
    val hbfUIState by hbfVM.uiState.collectAsState()
    val dndVM : DnDViewModel = viewModel()

    // filter for type
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround

    ) {
        items( items = DataSource.categoryTypes) { item ->
            Button(
                onClick = {
                    hbfVM.setType(item)
                    navController.navigate(Screens.StandardView.name)
                },
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = item.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() })
            }
        }
    }
}