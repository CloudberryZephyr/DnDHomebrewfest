package com.example.dndhomebrewfest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dndhomebrewfest.screens.CharacterCreationScreen
import com.example.dndhomebrewfest.screens.CharacterViewScreen
import com.example.dndhomebrewfest.screens.HomebrewCreationScreen
import com.example.dndhomebrewfest.screens.HomebrewViewScreen
import com.example.dndhomebrewfest.screens.StandardSearchScreen
import com.example.dndhomebrewfest.screens.StandardViewScreen
import com.example.dndhomebrewfest.ui.theme.DnDHomebrewfestTheme
import com.example.dndhomebrewfest.viewmodels.HBFViewModel
import com.example.dndhomebrewfest.viewmodels.RoomVM
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Screens () {
    CharacterView,
    CharacterCreation,
    HomebrewView,
    HomebrewCreation,
    StandardView,
    StandardSearch
}

class MainActivity : ComponentActivity() {
    private var currentCharIdToUpdate : Int? = null
    val roomVM = RoomVM.getInstance()
    val hbfVM : HBFViewModel by viewModels()

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            val imageUri = intent?.data
            val charID = currentCharIdToUpdate

            if (imageUri != null && charID != null) {
                val savedFile = saveImageToInternalStorage(imageUri)
                if (savedFile != null) {
                    val savedFileName = savedFile.name
                    roomVM.updateCharacterImage(charID, savedFileName)
                    hbfVM.onImageChange(savedFileName)
                    Log.d("MyTAG", "File saved to char $charID")
                } else {
                    Log.e("MyTAG", "Image saving error")
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
            currentCharIdToUpdate = null

        } else {
            currentCharIdToUpdate = null
        }
    }

    val imageLoader : (Int) -> Unit = { charIndex : Int ->
        currentCharIdToUpdate = charIndex
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

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
                        TopBar(screenName, navController::navigateUp)
                    },
                    bottomBar = {
                        BottomBar(screenName = screenName, navigate = navController::navigate)
                    }
                ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)
                            .paint(
                                painter = painterResource(R.drawable.background),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(Color.White, BlendMode.Softlight)
                            )) {
                            Homebrewery(navController, hbfVM, roomVM, imageLoader)
                        }
                }
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): File? {
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            // unique name for each character image
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "CHAR_IMG_$timeStamp.jpg"

            val destinationFile = File(filesDir, fileName)
            val outputStream = FileOutputStream(destinationFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return destinationFile

        } catch (e: IOException) {
            Log.e("SaveImage", "Error saving image", e)
            return null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(screenName : String, back : () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {},
        navigationIcon = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
fun Homebrewery(navController : NavHostController, hbfVM: HBFViewModel, roomVM: RoomVM, imageLoader : (Int) -> Unit, modifier: Modifier = Modifier) {
    NavHost (
        navController = navController,
        startDestination = Screens.CharacterView.name
    ) {
        composable(route = Screens.CharacterView.name) {
            CharacterViewScreen(
                createCharacter = {
                    navController.navigate(Screens.CharacterCreation.name)
                },
                hbfVM = hbfVM,
                navRight = {navController.navigate(Screens.HomebrewView.name)}
            )
        }

        composable(route = Screens.CharacterCreation.name) {
            // Character Creation
            CharacterCreationScreen(
                imageLoader = imageLoader,
                hbfVM = hbfVM,
                finish = {navController.navigateUp()}
            )
        }

        composable(route = Screens.HomebrewView.name) {
            HomebrewViewScreen(
                createHomebrew = {
                    navController.navigate(Screens.HomebrewCreation.name)
                },
                hbfVM = hbfVM,
                navRight = {navController.navigate(Screens.StandardSearch.name)},
                navLeft = {
                    hbfVM.setType("")
                    navController.navigate(Screens.CharacterView.name)
                }
            )
        }

        composable(route = Screens.HomebrewCreation.name) {
            // Homebrew Creation
            HomebrewCreationScreen(
                hbfVM = hbfVM,
                finish = {navController.navigateUp()}
            )
        }

        composable(route = Screens.StandardView.name) {
            // Standard View
            StandardViewScreen(hbfVM)
        }

        composable(route = Screens.StandardSearch.name) {
            StandardSearchScreen(navController,
                navLeft = {navController.navigate(Screens.HomebrewView.name)},
                hbfVM)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DnDHomebrewfestTheme {
//        Homebrewery(rememberNavController())
//    }
//}