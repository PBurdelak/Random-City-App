package com.pburdelak.randomcityapp.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @DispatchersDefault
    fun provideDispatchersDefault(): CoroutineDispatcher =
        Dispatchers.Default
}