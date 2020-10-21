package ru.haroncode.recycler.kit.core

interface Differ<T> {

    val currentList: List<T>

    fun submitList(items: List<T>)
}