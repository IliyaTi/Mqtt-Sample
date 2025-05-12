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

    DefineBrokerPage()

}

@Composable
fun DefineBrokerPage() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column (modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp)) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                label = { Text("Host") },
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                value = "",
                onValueChange = {},
                label = { Text("Port") }
            )

            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
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
    DefineBrokerPage()
}