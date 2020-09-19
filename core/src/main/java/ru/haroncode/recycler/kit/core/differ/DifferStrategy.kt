package ru.haroncode.recycler.kit.core.differ

import ru.haroncode.recycler.kit.core.datasource.DataSourceObserver

interface DifferStrategy<T> {

    fun calculateDiff(previous: List<T>, actual: List<T>): Result

    interface Result {

        fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver)
    }
}