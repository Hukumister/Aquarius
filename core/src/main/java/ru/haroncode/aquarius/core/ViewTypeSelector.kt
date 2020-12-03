package ru.haroncode.aquarius.core

interface ViewTypeSelector<T : Any> {

    fun createViewTypeFor(item: T): Int

    fun viewTypeFor(item: T): Int
}
