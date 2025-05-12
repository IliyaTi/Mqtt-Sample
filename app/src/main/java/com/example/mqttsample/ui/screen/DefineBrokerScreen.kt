package com.example.mqttsample.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DefineBrokerScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) { paddingValues ->

        Column (modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                label = { Text("Host") },
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                label = { Text("Port") }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            ) {
                Text("Create")
            }

        }

    }
}


@Preview
@Composable
fun Prev() {
    DefineBrokerScreen()
}