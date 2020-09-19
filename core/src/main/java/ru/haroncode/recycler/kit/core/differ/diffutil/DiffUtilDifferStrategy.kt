package ru.haroncode.recycler.kit.core.differ.diffutil

import androidx.recyclerview.widget.BatchingListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.ListUpdateCallback
import ru.haroncode.recycler.kit.core.datasource.DataSourceObserver
import ru.haroncode.recycler.kit.core.differ.DifferStrategy

class DiffUtilDifferStrategy<T>(
    private val callback: DiffUtil.Callback,
    private val detectMoves: Boolean = false
) : DifferStrategy<T> {

    override fun calculateDiff(previous: List<T>, actual: List<T>): DifferStrategy.Result {
        return if (previous.isEmpty()) {
            InsertRangeResult(0, actual.size)
        } else {
            val result = DiffUtil.calculateDiff(callback, detectMoves)
            DiffUtilResult(result)
        }
    }

    class DiffUtilResult(
        private val diffResult: DiffResult
    ) : DifferStrategy.Result {

        override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
            val adapterListUpdateCallback = DataSourceUpdateCallback(dataSourceObserver)
            val batchUpdateCallback = BatchingListUpdateCallback(adapterListUpdateCallback)
            diffResult.dispatchUpdatesTo(batchUpdateCallback)
        }
    }

    private class InsertRangeResult(
        private val position: Int,
        private val count: Int
    ) : DifferStrategy.Result {

        override fun dispatchUpdatesTo(dataSourceObserver: DataSourceObserver) {
            dataSourceObserver.onItemRangeInserted(position, count)
        }
    }

    private class DataSourceUpdateCallback(
        private val dataSourceObserver: DataSourceObserver
    ) : ListUpdateCallback {

        override fun onInserted(position: Int, count: Int) {
            dataSourceObserver.onItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            dataSourceObserver.onItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            dataSourceObserver.onItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            dataSourceObserver.onItemRangeChanged(position, count, payload)
        }
    }

}