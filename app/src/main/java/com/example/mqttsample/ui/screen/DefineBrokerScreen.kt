package com.example.mqttsample.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mqttsample.data.repository.Result
import com.example.mqttsample.ui.viewmodel.DefineBrokerScreenViewModel


@Composable
fun DefineBrokerScreen(navBackToMain: () -> Unit) {


    val viewModel = hiltViewModel<DefineBrokerScreenViewModel>()

    DefineBrokerPage(viewModel, navBackToMain)

}

@Composable
fun DefineBrokerPage(
    viewModel: DefineBrokerScreenViewModel,
    navBackToMain: () -> Unit
) {
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    
    LaunchedEffect(viewModel.saveState) {
        viewModel.saveState.collect { result ->
            when (result) {
                is Result.Inactive -> { /* TODO */ }
                is Result.Error -> { snackbarHostState.showSnackbar(message = result.exception.message.toString()) }
                is Result.Loading -> { /* TODO */ }
                is Result.Success -> { navBackToMain() }
            }
        }
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp)) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.name.value,
                onValueChange = { viewModel.name.value = it },
                label = { Text("Name") }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                value = viewModel.host.value,
                onValueChange = { viewModel.host.value = it },
                label = { Text("Host") },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                value = viewModel.port.value,
                onValueChange = { viewModel.port.value = it },
                label = { Text("Port") }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                onClick = {
                    viewModel.saveBroker()
                }
            ) {
                Text("Create")
            }

        }

    }
}


@Preview
@Composable
fun Prev() {
//    DefineBrokerPage()
}