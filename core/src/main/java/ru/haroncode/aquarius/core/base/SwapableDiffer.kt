package ru.haroncode.aquarius.core.base

import ru.haroncode.aquarius.core.Differ

interface SwapableDiffer<T> : Differ<T> {

    fun swap(fromPosition: Int, toPosition: Int)
}