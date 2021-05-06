package com.pburdelak.randomcityapp.hilt

import android.app.Application
import com.pburdelak.randomcityapp.room.AppDatabase
import com.pburdelak.randomcityapp.room.CityColorCombinationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @DispatchersDefault
    fun provideDispatchersDefault(): CoroutineDispatcher =
        Dispatchers.Default

    @Provides
    @DispatchersIO
    fun provideDispatchersIO(): CoroutineDispatcher =
        Dispatchers.IO

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase =
        AppDatabase.create(application)

    @Provides
    fun provideCityColorCombinationDao(database: AppDatabase): CityColorCombinationDao =
        database.cityColorCombinationDao()
}