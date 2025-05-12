package com.example.mqttsample.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mqttsample.data.source.local.entity.Broker
import kotlinx.coroutines.flow.Flow


@Dao
interface BrokerDao {

    @Query("SELECT * FROM broker")
    fun getAll(): Flow<List<Broker>>

    @Insert
    fun insert(vararg brokers: Broker)

    @Delete
    fun delete(broker: Broker)

}
