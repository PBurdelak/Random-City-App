package com.pburdelak.randomcityapp

import com.jraska.livedata.TestObserver
import com.pburdelak.randomcityapp.utils.livedata.Event

fun <T> TestObserver<Event<T>>.assertEventValue(expected: T?) {
    val value = value().getContent()
    if (value != expected) {
        val expectedValueAndClass = expected.toString() + " (class: " + (expected as? Any)?.let{ it::class.java.name } + ")"
        val valueAndClass = value.toString() + " (class: " + (value as? Any)?.let{ it::class.java.name } + ")"
        throw AssertionError("Expected: $expectedValueAndClass,\nActual: $valueAndClass")
    }
}