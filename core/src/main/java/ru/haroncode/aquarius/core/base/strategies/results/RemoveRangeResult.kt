package ru.haroncode.aquarius.core.base.strategies.results

import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.observer.DataSourceObserver

internal class RemoveRangeResult(
    private val position: Int,
    private val count: Int
) : DifferStrategy.Result {

    override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
        dataSourceObserver.onItemRangeRemoved(position, count)
    }
}
