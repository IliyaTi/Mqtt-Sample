package com.example.mqttsample.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.source.local.entity.Broker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefineBrokerScreenViewModel @Inject constructor(
    private val repository: BrokerRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var host by mutableStateOf("")
    var port by mutableStateOf("")

    fun saveBroker() {
        viewModelScope.launch {
            repository.insertBroker(Broker(name = name, host = host, port = port))
        }
    }

}