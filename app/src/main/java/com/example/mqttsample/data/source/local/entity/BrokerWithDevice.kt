package com.example.mqttsample.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BrokerWithDevice (
    @Embedded val broker: Broker,
    @Relation(
        parentColumn = "id",
        entityColumn = "brokerId"
    )
    val device: Device
)
