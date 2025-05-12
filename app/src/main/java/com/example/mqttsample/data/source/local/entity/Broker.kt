package com.example.mqttsample.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "broker")
data class Broker (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val host: String,
    val port: String
)