package com.example.mqttsample.data.source.local.dao

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "broker")
data class BrokerDao (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val host: String,
    val port: String
)