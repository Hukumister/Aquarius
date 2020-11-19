package ru.haroncode.aquarius.core.base.strategies

import ru.haroncode.aquarius.core.base.strategies.results.MoveRangeResult
import ru.haroncode.aquarius.core.base.strategies.results.RemoveRangeResult
import ru.haroncode.aquarius.core.observer.DataSourceObserver

abstract class DifferStrategy<T> {

    open fun removeAtPosition(position: Int): Result {
        return RemoveRangeResult(position, 1)
    }

    open fun swap(fromPosition: Int, toPosition: Int): Result {
        return MoveRangeResult(fromPosition, toPosition)
    }

    abstract fun calculateDiff(previous: List<T>, actual: List<T>): Result

    interface Result {

        fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver)
    }
}