package com.lza.hmh.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, crossinline block: ((T) -> Unit)) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            block.invoke(t)
            removeObserver(this)
        }
    })
}
inline fun <T> LiveData<T>.observeForeverOnce(crossinline block: ((T) -> Unit)) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T) {
            block.invoke(t)
            removeObserver(this)
        }
    })
}