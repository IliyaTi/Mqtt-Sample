package com.example.mqttsample.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mqttsample.data.source.local.entity.Device


@Dao
interface DeviceDao {

    @Query("SELECT * FROM device")
    fun getAll(): List<Device>

    @Insert
    fun insert(vararg devices: Device)

    @Delete
    fun delete(device: Device)

}

