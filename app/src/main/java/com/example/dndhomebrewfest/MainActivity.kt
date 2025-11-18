package com.example.dndhomebrewfest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme

enum class AppScreens(){
    HOME,
    CHARACTERCREATION,
    CHARACTERVIEW,
    HOMEBREWCREATION,
    HOMEBREWVIEW
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DnDHomebrewfestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomebreweryApp()
                }
            }
        }
    }
}

@Composable
fun HomebreweryApp(navController: NavHostController = rememberNavController()) {
    val VM : HBFViewModel = viewModel()
    val UIState by VM.uiState.collectAsState()
    NavHost(
        startDestination = AppScreens.HOME.name,
        navController = navController
    ) {
        composable(route = AppScreens.HOME.name){
            HomeScreen(
                createCharacter = {navController.navigate(AppScreens.CHARACTERCREATION.name)},
                viewCharacter = {navController.navigate(AppScreens.CHARACTERVIEW.name)},
                createHomebrew = {navController.navigate(AppScreens.HOMEBREWCREATION.name)},
                viewHomebrew = {navController.navigate(AppScreens.HOMEBREWVIEW.name)},
                VM = VM
            )
        }
        composable(route = AppScreens.CHARACTERCREATION.name){
            CharacterCreationScreen(
                goBack = {navController.navigateUp()}
            )
        }
        composable(route = AppScreens.CHARACTERVIEW.name){
            CharacterViewScreen(
                goBack = {navController.navigateUp()}
            )
        }
        composable(route = AppScreens.HOMEBREWCREATION.name){
            HomebrewCreationScreen(
                goBack = {navController.navigateUp()}
            )
        }
        composable(route = AppScreens.HOMEBREWVIEW.name){
            HomebrewViewScreen(
                goBack = {navController.navigateUp()}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DnDHomebrewfestTheme {
        HomebreweryApp()
    }
}