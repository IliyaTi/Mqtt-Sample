package com.example.mqttsample.data.repository

import com.example.mqttsample.data.source.local.dao.BrokerDao
import com.example.mqttsample.data.source.local.dao.DeviceDao
import com.example.mqttsample.data.source.local.entity.Broker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BrokerRepository @Inject constructor (
    private val brokerDao: BrokerDao,
    private val deviceDao: DeviceDao
) {

    suspend fun insertBroker(broker: Broker) {
        withContext(Dispatchers.IO) {
            brokerDao.insert(broker)
        }
    }

    fun getAllBrokersFlow() = brokerDao.getAll()




}