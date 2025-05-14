package com.example.mqttsample.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mqttsample.data.source.local.entity.Device
import com.example.mqttsample.ui.theme.MQTTSampleTheme
import com.example.mqttsample.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navToDefineBroker: () -> Unit,
    navToAddDevice: (Int) -> Unit,
    navToDevice: (Int) -> Unit
) {

    val viewModel = hiltViewModel<MainScreenViewModel>()

//    MainPage(
//        brokers = viewModel.brokers,
//        navToDefineBroker = navToDefineBroker
//    )

    MainPage(
        viewModel = viewModel,
        navToDefineBroker = navToDefineBroker,
        navToAddDevice = navToAddDevice,
        navToDevice = navToDevice
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    viewModel: MainScreenViewModel,
    navToDefineBroker: () -> Unit,
    navToAddDevice: (Int) -> Unit,
    navToDevice: (Int) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }
    val items by viewModel.brokers.collectAsState(emptyList())

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchDevices()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (viewModel.selectedBroker.value == null) {
                        scope.launch { snackbarHostState.showSnackbar(message = "Select a Broker first") }
                        return@FloatingActionButton
                    }
                    navToAddDevice(viewModel.selectedBroker.value!!.id)
                }
            ) {
                Icon(Icons.Filled.Add, "fab")
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
        ) {

            val (brokers, list) = createRefs()

            ExposedDropdownMenuBox(
                modifier = Modifier.constrainAs(brokers) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(PrimaryNotEditable),
                    readOnly = true,
                    value = viewModel.selectedBroker.value?.name ?: "",
                    label = { Text("Select target Broker") },
                    onValueChange = { },
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
                            text = {
                                Column {
                                    Text(it.name ?: "", fontWeight = FontWeight.Bold)
                                    Text(it.host)
                                }
                            },
                            onClick = {
                                expanded = false
                                viewModel.selectedBroker.value = it
                            }
                        )
                    }

                }
            }

            LazyColumn(
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(brokers.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
            ) {
                items(viewModel.devices.value) {
                    ListItem(it) {
                        navToDevice(it)
                    }
                }
            }

        }
    }
}


@Composable
fun ListItem(device: Device, onClick: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick(device.id) }.padding(18.dp)) {
        Column {
            Text(device.name ?: "")
        }
    }
}


@Preview
@Composable
fun MainPagePreview() {
    MQTTSampleTheme {
//        MainPage(flowOf(), {})
    }
}

