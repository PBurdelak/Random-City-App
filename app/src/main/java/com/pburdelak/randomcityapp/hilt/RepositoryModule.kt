package com.pburdelak.randomcityapp.hilt

import com.pburdelak.randomcityapp.repository.ListRepository
import com.pburdelak.randomcityapp.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideListRepository(
        repository: Repository
    ): ListRepository
}