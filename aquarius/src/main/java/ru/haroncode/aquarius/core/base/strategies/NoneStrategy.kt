package ru.haroncode.aquarius.core.base.strategies

import ru.haroncode.aquarius.core.observer.DataListUpdateCallback

internal class NoneStrategy<T : Any> : DifferStrategy<T>() {

    override fun calculateDiff(
        previous: List<T>,
        actual: List<T>
    ): Result = NoneDiffResult(previous.size, actual.size)

    private class NoneDiffResult(
        private val previousCount: Int,
        private val actualCount: Int
    ) : Result {

        override fun dispatchUpdatesTo(
            dataListUpdateCallback: DataListUpdateCallback
        ) = when {
            previousCount == 0 && actualCount > 0 -> dataListUpdateCallback.onInserted(0, actualCount)
            previousCount > 0 && actualCount == 0 -> dataListUpdateCallback.onRemoved(0, previousCount)
            else -> dataListUpdateCallback.onChanged()
        }
    }
}
