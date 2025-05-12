package com.example.mqttsample.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mqttsample.data.source.local.entity.Broker
import com.example.mqttsample.ui.theme.MQTTSampleTheme
import com.example.mqttsample.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun MainScreen(navToDefineBroker: () -> Unit) {

    val viewModel = hiltViewModel<MainScreenViewModel>()

    MainPage(
        brokers = viewModel.brokers,
        navToDefineBroker = navToDefineBroker
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    brokers: Flow<List<Broker>>,
    navToDefineBroker: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val items by brokers.collectAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(20.dp),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(PrimaryNotEditable),
                    readOnly = true,
                    value = "",
                    label = { Text("Broker") },
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("Add a new broker") },
                        onClick = {
                            expanded = false
                            navToDefineBroker()
                        }
                    )

                    items.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name ?: "") },
                            onClick = {
                                expanded = false
                            }
                        )
                    }
                    
                }
            }
        }
    }
}


@Preview
@Composable
fun MainPagePreview() {
    MQTTSampleTheme {
        MainPage(flowOf(), {})
    }
}

