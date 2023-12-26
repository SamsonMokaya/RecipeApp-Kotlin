package com.ahmedapps.roomdatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.ahmedapps.roomdatabase.data.RecipesDatabase
import com.ahmedapps.roomdatabase.presentation.AddRecipeScreen
import com.ahmedapps.roomdatabase.presentation.RecipesScreen
import com.ahmedapps.roomdatabase.presentation.RecipesViewModel
import com.ahmedapps.roomdatabase.ui.theme.RoomDatabaseTheme

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            RecipesDatabase::class.java,
            "recipes.db"
        ).build()
    }

    private val viewModel by viewModels<RecipesViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return RecipesViewModel(database.dao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController= navController, startDestination = "RecipesScreen") {
                        composable("RecipesScreen") {
                            RecipesScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent,
                                onItemClick = { recipeId ->
                                    navController.navigate("ViewRecipeScreen/$recipeId")
                                }
                            )
                        }
                        composable("AddRecipeScreen") {
                            AddRecipeScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }

                    }

                }
            }
        }
    }
}

