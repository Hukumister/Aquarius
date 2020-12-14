package ru.haroncode.aquarius.core.base.strategies.results

import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.observer.DataListUpdateCallback

internal class RemoveRangeResult(
    private val position: Int,
    private val count: Int
) : DifferStrategy.Result {

    override fun dispatchUpdatesTo(dataListUpdateCallback: DataListUpdateCallback) {
        dataListUpdateCallback.onRemoved(position, count)
    }
}
