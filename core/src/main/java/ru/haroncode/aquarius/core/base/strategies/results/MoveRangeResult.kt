package ru.haroncode.aquarius.core.base.strategies.results

import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.observer.DataSourceObserver

internal class MoveRangeResult(
    private val fromPosition: Int,
    private val toPosition: Int
) : DifferStrategy.Result {

    override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
        dataSourceObserver.onItemMoved(fromPosition, toPosition)
    }
}