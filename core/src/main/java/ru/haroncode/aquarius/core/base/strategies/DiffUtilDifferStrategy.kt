package ru.haroncode.aquarius.core.base.strategies

import androidx.recyclerview.widget.BatchingListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import ru.haroncode.aquarius.core.observer.DataSourceObserver
import ru.haroncode.aquarius.core.observer.DataSourceUpdateCallback

class DiffUtilDifferStrategy<T : Any>(
    private val itemCallback: DiffUtil.ItemCallback<T>,
    private val detectMoves: Boolean = false
) : DifferStrategy<T>() {

    override fun calculateDiff(previous: List<T>, actual: List<T>): Result {
        return if (previous.isNotEmpty()) {
            val callback = SimpleDiffCallback(itemCallback, previous, actual)
            val result = DiffUtil.calculateDiff(callback, detectMoves)
            DiffUtilResult(result)
        } else {
            InsertRangeResult(0, actual.size)
        }
    }

    private class SimpleDiffCallback<E>(
        private val itemDiffCallback: DiffUtil.ItemCallback<E>,
        private val previous: List<E>,
        private val actual: List<E>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = previous.size

        override fun getNewListSize(): Int = actual.size

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean = itemDiffCallback.areItemsTheSame(previous[oldItemPosition], actual[newItemPosition])

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean = itemDiffCallback.areContentsTheSame(previous[oldItemPosition], actual[newItemPosition])

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Any? = itemDiffCallback.getChangePayload(previous[oldItemPosition], actual[newItemPosition])
    }

    private class DiffUtilResult(
        private val diffResult: DiffResult
    ) : Result {

        override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
            val adapterListUpdateCallback = DataSourceUpdateCallback(dataSourceObserver)
            val batchUpdateCallback = BatchingListUpdateCallback(adapterListUpdateCallback)
            diffResult.dispatchUpdatesTo(batchUpdateCallback)
        }
    }

    private class InsertRangeResult(
        private val position: Int,
        private val count: Int
    ) : Result {

        override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
            dataSourceObserver.onItemRangeInserted(position, count)
        }
    }
}