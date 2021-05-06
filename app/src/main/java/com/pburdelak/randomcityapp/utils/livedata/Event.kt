package com.pburdelak.randomcityapp.utils.livedata

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContent(): T {
        hasBeenHandled = true
        return content
    }
}