package com.harvest.core_base.coroutine

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


fun ComponentActivity.launch(
    defaultDispatcher: CoroutineContext = Dispatchers.Main,
    block: suspend () -> Unit
) {
    lifecycleScope.launch(defaultDispatcher) {
        block.invoke()
    }
}

fun Fragment.launch(
    defaultDispatcher: CoroutineContext = Dispatchers.Main,
    block: suspend () -> Unit
) {
    lifecycleScope.launch(defaultDispatcher) {
        block.invoke()
    }
}