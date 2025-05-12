package com.example.mqttsample.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mqttsample.data.source.local.dao.BrokerDao
import com.example.mqttsample.data.source.local.dao.DeviceDao
import com.example.mqttsample.data.source.local.entity.Broker
import com.example.mqttsample.data.source.local.entity.Device


@Database(entities = [Broker::class, Device::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun BrokerDao(): BrokerDao
    abstract fun DeviceDao(): DeviceDao

    companion object {

        // TODO: temp: Should provide the db using dagger-hilt

        private var instance: AppDB? = null

        fun getDatabase(context: Context): AppDB {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "TakhfifdarDatabase"
                )
                    .addMigrations() // no migrations yet, it's just a reminder for me
                    .build()
                AppDB.instance = instance
                return instance
            }
        }

    }






}
