package com.example.mqttsample.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mqttsample.data.repository.Result
import com.example.mqttsample.ui.viewmodel.AddDeviceDialogViewModel


@Composable
fun AddDeviceDialog(
    viewModel: AddDeviceDialogViewModel,
    brokerId: Int,
    navBack: () -> Unit
) {

    val context = LocalContext.current

    Card {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                value = viewModel.name.value,
                onValueChange = { viewModel.name.value = it },
                label = { Text("Name") }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                value = viewModel.topic.value,
                onValueChange = { viewModel.topic.value = it },
                label = { Text("Topic") }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                onClick = {
                    viewModel.saveDevice(brokerId)
                }
            ) {
                Text("Add")
            }


        }
    }



    LaunchedEffect(viewModel.saveState) {
        viewModel.saveState.collect { result ->
            when (result) {
                is Result.Success -> navBack()
                is Result.Error -> Toast.makeText(context, result.exception.toString(), Toast.LENGTH_LONG).show()
                is Result.Inactive -> {  }
                is Result.Loading -> {  }
            }
        }
    }


}