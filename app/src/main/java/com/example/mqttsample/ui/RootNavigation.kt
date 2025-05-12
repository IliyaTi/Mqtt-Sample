package com.example.mqttsample.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mqttsample.ui.screen.DefineBrokerScreen
import com.example.mqttsample.ui.screen.MainScreen
import kotlinx.serialization.Serializable


@Serializable
object Main

@Serializable
object DefineBroker


@Composable
fun RootNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Main) {
        composable<Main> {
            MainScreen { navController.navigate(DefineBroker) }
        }

        composable<DefineBroker> {
            DefineBrokerScreen()
        }
    }
}


