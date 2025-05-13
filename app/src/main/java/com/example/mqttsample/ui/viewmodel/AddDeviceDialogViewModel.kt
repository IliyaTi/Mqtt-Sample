package com.example.mqttsample.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.repository.Result
import com.example.mqttsample.data.source.local.entity.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddDeviceDialogViewModel @Inject constructor(
    private val repository: BrokerRepository
) : ViewModel() {

    val name = mutableStateOf("")
    val topic = mutableStateOf("")

    private val _saveState = MutableSharedFlow<Result<Long>>()
    val saveState = _saveState.asSharedFlow()

    fun saveDevice(brokerId: Int) {
        viewModelScope.launch {
            _saveState.emit(repository.insertDevice(Device(name = name.value, topic = topic.value, brokerId = brokerId)))
        }
    }



}