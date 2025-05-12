package com.example.mqttsample.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BrokerWithDevices (
    @Embedded val broker: Broker,
    @Relation(
        parentColumn = "id",
        entityColumn = "brokerId"
    )
    val devices: List<Device>
)
