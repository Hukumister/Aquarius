package ru.haroncode.aquarius.core

interface Notifier<T> {

    fun change(position: Int, count: Int = 1, payload: Any? = null)

    fun change(item: T, payload: Any? = null)
}
