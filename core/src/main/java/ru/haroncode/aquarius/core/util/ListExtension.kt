package ru.haroncode.aquarius.core.util

import androidx.collection.SparseArrayCompat
import java.util.*

fun <E> List<E>.moveSwap(fromPosition: Int, toPosition: Int): List<E> {
    val mutableList = toMutableList()
    val range = if (fromPosition < toPosition) {
        fromPosition until toPosition
    } else {
        fromPosition downTo toPosition + 1
    }

    for (index in range) {
        Collections.swap(mutableList, index, index + 1)
    }
    return mutableList
}

fun <E> SparseArrayCompat<E>.forEach(consumer: (E) -> Unit) {
    for (index in 0 until size()) {
        val value = get(index) ?: continue
        consumer(value)
    }
}