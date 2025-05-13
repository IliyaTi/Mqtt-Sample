package com.example.mqttsample.di

import android.content.Context
import com.example.mqttsample.data.repository.BrokerRepository
import com.example.mqttsample.data.repository.RemoteRepositoryFactory
import com.example.mqttsample.data.source.local.AppDB
import com.example.mqttsample.data.source.local.dao.BrokerDao
import com.example.mqttsample.data.source.local.dao.DeviceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDB =
        AppDB.getDatabase(context)

    @Provides
    @Singleton
    fun provideBrokerDao(database: AppDB): BrokerDao = database.BrokerDao()

    @Provides
    @Singleton
    fun provideDeviceDao(database: AppDB): DeviceDao = database.DeviceDao()

    @Provides
    @Singleton
    fun provideBrokerRepository(brokerDao: BrokerDao, deviceDao: DeviceDao): BrokerRepository {
        return BrokerRepository(brokerDao, deviceDao)
    }

    @Provides
    fun provideRemoteRepositoryFactory(): RemoteRepositoryFactory {
        return RemoteRepositoryFactory()
    }

}
