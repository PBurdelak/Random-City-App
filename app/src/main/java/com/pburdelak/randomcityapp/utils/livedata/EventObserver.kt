package com.pburdelak.randomcityapp.utils.livedata

import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        if (!event.hasBeenHandled) {
            onEventUnhandledContent(event.getContent())
        }
    }
}