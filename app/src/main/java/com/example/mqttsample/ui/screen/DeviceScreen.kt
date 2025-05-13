package com.example.mqttsample.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttsample.ui.viewmodel.DeviceScreenViewModel


@Composable
fun DeviceScreen(
    deviceId: Int,
    viewModel: DeviceScreenViewModel
) {

    LaunchedEffect(true) {
        viewModel.populateDevice(deviceId)
    }

    Scaffold (
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column (
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Name: ${viewModel.device.value?.device?.name ?: ""}")
            Text("Topic: ${viewModel.device.value?.device?.topic ?: ""}")
            Text("Broker: ${viewModel.device.value?.broker?.name ?: ""}")

            Text(viewModel.connection.value, modifier = Modifier.padding(top = 26.dp), fontSize = 20.sp)

            Row(Modifier.padding(top = 40.dp)) {
                Text("State: ")
                Text(if(viewModel.powerState.value == true) "On" else "Off")
            }

            Row {
                Text("Brightness: ")
                Text("${viewModel.brightness.value}%")
            }

            Text("Modify", modifier = Modifier.padding(top = 30.dp))



        }
    }

}