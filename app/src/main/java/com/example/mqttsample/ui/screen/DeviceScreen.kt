package com.example.mqttsample.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttsample.data.repository.ConnectionState
import com.example.mqttsample.data.repository.MQTTMessage
import com.example.mqttsample.ui.viewmodel.DeviceScreenViewModel


@Composable
fun DeviceScreen(
    deviceId: Int,
    viewModel: DeviceScreenViewModel
) {

    val connection by viewModel.connectionState.collectAsState()
    val messages by viewModel.messages.collectAsState(MQTTMessage("", ""))

    LaunchedEffect(true) {
        viewModel.populateDevice(deviceId)
    }

    Scaffold (
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Name: ${viewModel.deviceWithBroker.value?.device?.name ?: ""}")
            Text("Topic: ${viewModel.deviceWithBroker.value?.device?.topic ?: ""}")
            Text("Broker: ${viewModel.deviceWithBroker.value?.broker?.name ?: ""}")

            Text(text = when (connection) {
                ConnectionState.Connecting -> "Connecting..."
                ConnectionState.Connected -> "Connected"
                ConnectionState.Disconnected -> "Disconnected"
                is ConnectionState.Error -> "Error"
            },
                modifier = Modifier.padding(top = 26.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold
            )

            Row(Modifier.padding(top = 40.dp)) {
                Text("State: ")
//                Text(if(viewModel.powerState.value == true) "On" else "Off")
            }

            Row {
                Text("Brightness: ")
                Text( messages.payload )
            }

            Text("Modify", modifier = Modifier.padding(top = 30.dp))

            OutlinedTextField(
                value = viewModel.modifyField,
                onValueChange = { viewModel.modifyField = it },
                label = { Text("New value") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 20.dp, end = 20.dp)
            )

            Button(
                onClick = {
                    viewModel.publish()
                }
            ) {
                Text("emit")
            }

        }
    }

}