package ru.haroncode.recycler.kit.core.base.strategies

import ru.haroncode.recycler.kit.core.observer.DataSourceObserver

interface DifferStrategy<T> {

    fun calculateDiff(previous: List<T>, actual: List<T>): Result

    interface Result {

        fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver)
    }
}