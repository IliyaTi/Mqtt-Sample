package com.example.mqttsample.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mqttsample.data.repository.BrokerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repo: BrokerRepository
) : ViewModel() {

    val brokers = repo.getAllBrokersFlow()

}