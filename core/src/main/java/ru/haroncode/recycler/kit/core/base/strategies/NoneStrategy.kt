package ru.haroncode.recycler.kit.core.base.strategies

import ru.haroncode.recycler.kit.core.observer.DataSourceObserver

internal class NoneStrategy<T : Any> :
    DifferStrategy<T> {

    override fun calculateDiff(
        previous: List<T>,
        actual: List<T>
    ): DifferStrategy.Result = NoneDiffResult(previous.size, actual.size)

    private class NoneDiffResult(
        private val previousCount: Int,
        private val actualCount: Int
    ) : DifferStrategy.Result {

        override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
            if (previousCount == 0 && actualCount > 0) {
                dataSourceObserver.onItemRangeInserted(0, actualCount)
            } else if (previousCount > 0 && actualCount == 0) {
                dataSourceObserver.onItemRangeRemoved(0, previousCount)
            } else {
                dataSourceObserver.onChanged()
            }
        }
    }
}