package com.example.mqttsample.data.source.local.dao

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("device")
data class DeviceDao (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val topic: String,
    @ForeignKey(BrokerDao::class, )
    val brokerId: Int


)