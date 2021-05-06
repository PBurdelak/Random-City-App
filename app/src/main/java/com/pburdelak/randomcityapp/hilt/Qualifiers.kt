package com.pburdelak.randomcityapp.hilt

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatchersDefault

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatchersIO