package com.pburdelak.randomcityapp.utils

import timber.log.Timber

fun Throwable.log() =
    Timber.e(this)