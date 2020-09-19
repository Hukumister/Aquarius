package ru.haroncode.recycler.kit.core.differ

import ru.haroncode.recycler.kit.core.datasource.DataSourceObserver

open class BaseDiffer<T>(
    private val differStrategy: DifferStrategy<T>,
    private val dataSourceObserver: DataSourceObserver
) {

    private val actualItems = arrayListOf<T>()

    val currentList: List<T>
        get() = actualItems

    open fun submitList(items: List<T>) {
        val calculateDiff = differStrategy.calculateDiff(actualItems, items)
        calculateDiff.dispatchUpdatesTo(dataSourceObserver)
    }
}