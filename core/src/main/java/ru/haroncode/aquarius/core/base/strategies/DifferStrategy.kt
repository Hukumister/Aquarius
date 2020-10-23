package ru.haroncode.aquarius.core.base.strategies

import ru.haroncode.aquarius.core.observer.DataSourceObserver

interface DifferStrategy<T> {

    fun calculateDiff(previous: List<T>, actual: List<T>): Result

    interface Result {

        fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver)
    }
}