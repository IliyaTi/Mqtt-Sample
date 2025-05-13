package com.example.mqttsample.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mqttsample.data.source.local.entity.BrokerWithDevice
import com.example.mqttsample.data.source.local.entity.Device


@Dao
interface DeviceDao {

    @Query("SELECT * FROM device")
    fun getAll(): List<Device>

    @Query("SELECT * FROM device WHERE id = :id")
    fun getDevice(id: Int): Device?

    @Query("SELECT * FROM device WHERE brokerId = :brokerId;")
    fun getBrokerDevices(brokerId: Int): List<Device>

    @Query("SELECT * FROM device LEFT JOIN broker ON device.brokerId = broker.id WHERE device.id = :deviceId")
    fun getDeviceAndBroker(deviceId: Int): BrokerWithDevice

    @Insert
    fun insert(device: Device): Long

    @Delete
    fun delete(device: Device)

}

