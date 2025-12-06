package com.example.dndhomebrewfest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.dndhomebrewfest.viewmodels.RoomVM
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
                        TopBar(navController::navigateUp)
                    },
                    bottomBar = {
                        BottomBar(screenName = screenName, navigate = navController::navigate)
                    }
                ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Homebrewery(navController)
                        }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(back : () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {},
        navigationIcon = {
            Row(
                modifier = modifier.fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // back button
                IconButton(
                    onClick = back
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = null
                    )
                }

                Spacer(modifier = modifier.width(40.dp))

                Text("Homebrewery",
                    style = MaterialTheme.typography.displaySmall)


            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
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
fun Homebrewery(navController : NavHostController) {
    val roomVM = RoomVM.getInstance()
    val hbfVM : HBFViewModel = viewModel()

    NavHost (
        navController = navController,
        startDestination = Screens.CharacterView.name
    ) {
        composable(route = Screens.CharacterView.name) {
            // Character View
        }

        composable(route = Screens.CharacterCreation.name) {
            // Character Creation
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DnDHomebrewfestTheme {
        Homebrewery(rememberNavController())
    }
}