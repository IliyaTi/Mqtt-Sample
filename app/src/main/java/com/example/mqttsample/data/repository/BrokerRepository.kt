package com.example.mqttsample.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.mqttsample.data.source.local.dao.BrokerDao
import com.example.mqttsample.data.source.local.dao.DeviceDao
import com.example.mqttsample.data.source.local.entity.Broker
import com.example.mqttsample.data.source.local.entity.BrokerWithDevice
import com.example.mqttsample.data.source.local.entity.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BrokerRepository @Inject constructor (
    private val brokerDao: BrokerDao,
    private val deviceDao: DeviceDao
) {

    suspend fun insertBroker(broker: Broker): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val rowId = brokerDao.insert(broker)
                if (rowId != -1L) Result.Success(rowId)
                else Result.Error(Exception("Insert failed (returned -1)"))
            } catch (e: SQLiteConstraintException) {
                Result.Error(Exception("Constraint violated: ${e.message}"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    fun getAllBrokersFlow() = brokerDao.getAll()

    suspend fun getBrokerDevices(brokerId: Int): List<Device> {
        return withContext(Dispatchers.IO) {
            deviceDao.getBrokerDevices(brokerId)
        }
    }

    suspend fun insertDevice(device: Device): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val rowId = deviceDao.insert(device)
                if (rowId != -1L) Result.Success(rowId)
                else Result.Error(Exception("Insertion failed; returned -1"))
            } catch (e: SQLiteConstraintException) {
                Result.Error(Exception("Constraint violated: ${e.message}"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getDevice(id: Int): Result<Device> {
        return withContext(Dispatchers.IO) {
            try {
                val device = deviceDao.getDevice(id)
                if (device != null) Result.Success(device)
                else Result.Error(Exception("Select failed; returned null"))
            } catch (e: SQLiteConstraintException) {
                Result.Error(Exception("Constraint violated: ${e.message}"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getDeviceWithBroker(devideId: Int): BrokerWithDevice {
        return withContext(Dispatchers.IO) {
            deviceDao.getDeviceAndBroker(devideId)
        }
    }




}