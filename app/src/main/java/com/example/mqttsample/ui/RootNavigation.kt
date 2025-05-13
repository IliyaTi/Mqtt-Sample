package com.example.mqttsample.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mqttsample.ui.screen.AddDeviceDialog
import com.example.mqttsample.ui.screen.DefineBrokerScreen
import com.example.mqttsample.ui.screen.DeviceScreen
import com.example.mqttsample.ui.screen.MainScreen
import com.example.mqttsample.ui.viewmodel.AddDeviceDialogViewModel
import com.example.mqttsample.ui.viewmodel.DeviceScreenViewModel
import kotlinx.serialization.Serializable


@Serializable
object Main

@Serializable
object DefineBroker

@Serializable
object AddDevice

@Composable
fun RootNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Main) {
        composable<Main> {
            MainScreen(
                navToDefineBroker = { navController.navigate(DefineBroker) },
                navToAddDevice = { brokerId ->
                    navController.navigate("AddDevice/$brokerId")
                },
                navToDevice = { deviceId ->
                    navController.navigate("DeviceScreen/$deviceId")
                }
            )
        }

        composable<DefineBroker> {
            DefineBrokerScreen { navController.popBackStack() }
        }

        dialog(
            route = "AddDevice/{brokerId}",
            arguments = listOf(
                navArgument("brokerId") { type = NavType.IntType }
            )
        ) {
            it.arguments?.getInt("brokerId")?.let {
                AddDeviceDialog(
                    hiltViewModel<AddDeviceDialogViewModel>(),
                    it,
                    { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "DeviceScreen/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
        ) {
            it.arguments?.getInt("deviceId")?.let {
                DeviceScreen(it, hiltViewModel<DeviceScreenViewModel>())
            }
        }

    }
}


