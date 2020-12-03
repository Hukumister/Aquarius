package ru.haroncode.aquarius.core

interface MutableDiffer<T> : Differ<T> {

    fun removeAt(position: Int)

    fun addAt(position: Int, item: T)

    fun addAllAt(position: Int, items: List<T>)

    fun add(item: T)

    fun addAll(items: List<T>)

    fun replace(position: Int, item: T)
}
