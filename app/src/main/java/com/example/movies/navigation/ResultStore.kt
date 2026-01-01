package com.example.movies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

class ResultStore {
    val map = mutableMapOf<String, Any?>()

    inline fun <reified T> setResult(value: T, resultKey: String = T::class.toString()) {
        map[resultKey] = value
    }

    inline fun <reified T> getResult(): T? {
        return map[T::class.toString()] as T?
    }

    inline fun <reified T> remove() {
        map.remove(T::class.toString())
    }

    companion object {
        val saver: Saver<ResultStore, *> = Saver(
            save = { it.map },
            restore = { ResultStore().apply { map.putAll(it) } },
        )
    }
}

@Composable
fun rememberResultStore(): ResultStore {
    return rememberSaveable(saver = ResultStore.saver) {
        ResultStore()
    }
}