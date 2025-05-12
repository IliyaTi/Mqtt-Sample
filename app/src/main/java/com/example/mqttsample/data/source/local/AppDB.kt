package com.example.mqttsample.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [])
abstract class AppDB : RoomDatabase() {

    abstract fun BrokerDao(): BrokerDao
    abstract fun DeviceDao(): DeviceDao


}
