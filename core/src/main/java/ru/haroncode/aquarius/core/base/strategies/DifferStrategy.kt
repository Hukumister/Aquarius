package ru.haroncode.aquarius.core.base.strategies

import ru.haroncode.aquarius.core.base.strategies.results.AddRangeResult
import ru.haroncode.aquarius.core.base.strategies.results.ChangeRangeResult
import ru.haroncode.aquarius.core.base.strategies.results.MoveRangeResult
import ru.haroncode.aquarius.core.base.strategies.results.RemoveRangeResult
import ru.haroncode.aquarius.core.observer.DataSourceObserver

abstract class DifferStrategy<T> {

    open fun addAtPosition(position: Int, count: Int = 1): Result {
        return AddRangeResult(position, count)
    }

    open fun removeAtPosition(position: Int): Result {
        return RemoveRangeResult(position, 1)
    }

    open fun move(fromPosition: Int, toPosition: Int): Result {
        return MoveRangeResult(fromPosition, toPosition)
    }

    open fun change(position: Int, count: Int = 1, payload: Any? = null): Result {
        return ChangeRangeResult(position, count, payload)
    }

    abstract fun calculateDiff(previous: List<T>, actual: List<T>): Result

    interface Result {

        fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver)
    }
}
