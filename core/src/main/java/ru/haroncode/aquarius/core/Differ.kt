package ru.haroncode.aquarius.core

interface Differ<T> {

    val currentList: List<T>

    fun submitList(items: List<T>)
}
