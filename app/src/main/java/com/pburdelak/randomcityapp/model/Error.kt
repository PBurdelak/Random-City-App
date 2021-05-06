package com.pburdelak.randomcityapp.model

import com.pburdelak.randomcityapp.R

sealed class Error {
    abstract val messageRes: Int

    object Retrieving: Error() {
        override val messageRes: Int = R.string.error_retrieving
    }
    object Saving: Error() {
        override val messageRes: Int = R.string.error_saving
    }
    object Deleting: Error() {
        override val messageRes: Int = R.string.error_deleting
    }
}