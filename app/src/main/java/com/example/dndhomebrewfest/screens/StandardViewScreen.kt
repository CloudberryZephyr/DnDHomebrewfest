package com.example.dndhomebrewfest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dndhomebrewfest.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.dndhomebrewfest.Homebrewery
import com.example.dndhomebrewfest.data.DataSource
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.HBFViewModel

@Composable
fun StandardViewScreen(modifier : Modifier = Modifier) {

    val hbfVM : HBFViewModel = viewModel()
    val hbfUIState by hbfVM.uiState.collectAsState()


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = hbfUIState.current_filter,
            onValueChange = hbfVM::onCurrentFilterChanged,
            shape = shapes.large,
            label = { Text("Search") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    modifier = modifier.size(30.dp)
                )
            }
        )
    }

//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = modifier.fillMaxSize(),
//            contentPadding = PaddingValues(4.dp)
//
//        ) {
//            items(items = )
//        }

}

@Preview(showBackground = true)
@Composable
fun StandardPreview() {
    DnDHomebrewfestTheme {
        StandardViewScreen()
    }
}