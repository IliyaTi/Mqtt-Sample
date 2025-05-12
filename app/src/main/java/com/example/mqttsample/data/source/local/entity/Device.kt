package com.example.mqttsample.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("device")
data class Device (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val topic: String,
    val brokerId: Int
)