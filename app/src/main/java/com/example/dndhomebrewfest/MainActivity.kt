package com.example.dndhomebrewfest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.DnDViewModel
import com.example.dndhomebrewfest.viewmodels.RoomVM
import com.example.dndhomebrewfest.data.Character
import com.example.dndhomebrewfest.screens.CharacterCreationScreen
import com.example.dndhomebrewfest.screens.CharacterViewScreen
import com.example.dndhomebrewfest.screens.StandardSearchScreen
import com.example.dndhomebrewfest.screens.StandardViewScreen
import com.example.dndhomebrewfest.viewmodels.HBFViewModel

enum class Screens () {
    CharacterView,
    CharacterCreation,
    HomebrewView,
    HomebrewCreation,
    StandardView,
    StandardSearch
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DnDHomebrewfestTheme {

                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val screenName = backStackEntry?.destination?.route ?: ""

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {

                    },
                    bottomBar = {
                        BottomBar(screenName = screenName, navigate = navController::navigate)
                    }) { innerPadding ->

                    Homebrewery(navController)
                }
            }
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, screenName : String, back : () -> Unit) {

    // back button

    // screen name
}


@Composable
fun BottomBar(modifier: Modifier = Modifier, screenName : String, navigate : (route : String) -> Unit) {
    NavigationBar(
        modifier = modifier.height(100.dp)
    ) {
        // characters
        NavigationBarItem(
            selected = screenName == Screens.CharacterView.name,
            onClick = {
                navigate(Screens.CharacterView.name)
            },
            icon = {
                Image(painter = painterResource(R.drawable.character_icon),
                    contentDescription = null
                )
            },
            modifier = modifier.fillMaxHeight()
        )

        // homebrew
        NavigationBarItem(
            selected = screenName == Screens.HomebrewView.name,
            onClick = {
                navigate(Screens.HomebrewView.name)
            },
            icon = {
                Image(painter = painterResource(R.drawable.barrel),
                    contentDescription = null,
                    modifier = modifier.padding(vertical = 10.dp)
                )
            },
            modifier = modifier.fillMaxHeight()

        )

        // standard
        NavigationBarItem(
            selected = screenName == Screens.StandardSearch.name,
            onClick = {
                navigate(Screens.StandardSearch.name)
            },
            icon = {
                Image(painter = painterResource(R.drawable.book),
                    contentDescription = null,
                    modifier = modifier.padding(vertical = 10.dp)
                )
            },
            modifier = modifier.fillMaxHeight()

        )
    }
}

@Composable
fun Homebrewery(navController : NavHostController, modifier: Modifier = Modifier) {
    val roomVM = RoomVM.getInstance()
    val hbfVM : HBFViewModel = viewModel()

    NavHost (
        navController = navController,
        startDestination = Screens.CharacterView.name
    ) {
        composable(route = Screens.CharacterView.name) {
            // Character View
            CharacterViewScreen(
                createCharacter = {navController.navigate(Screens.CharacterCreation.name)}
            )
        }

        composable(route = Screens.CharacterCreation.name) {
            // Character Creation
            CharacterCreationScreen(hbfVM, modifier = Modifier)
        }

        composable(route = Screens.HomebrewView.name) {
            // Homebrew View
        }

        composable(route = Screens.HomebrewCreation.name) {
            // Homebrew Creation
        }

        composable(route = Screens.StandardView.name) {
            // Standard View
            StandardViewScreen(hbfVM)
        }

        composable(route = Screens.StandardSearch.name) {
            StandardSearchScreen(navController, hbfVM)
        }
    }
}

@Composable
fun dbCard(character : Character, modifier: Modifier = Modifier) {

    Card(modifier = modifier.fillMaxWidth()) {
        Text(character.name)
        Text(character.character_class)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DnDHomebrewfestTheme {
        Homebrewery(rememberNavController())
    }
}