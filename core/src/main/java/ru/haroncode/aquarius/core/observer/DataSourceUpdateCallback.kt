package ru.haroncode.aquarius.core.observer

import androidx.recyclerview.widget.ListUpdateCallback

internal class DataSourceUpdateCallback(
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