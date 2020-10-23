package ru.haroncode.aquarius.core.observer

import androidx.recyclerview.widget.RecyclerView

class AdapterDataSourceObserver(
    private val adapter: RecyclerView.Adapter<*>
) : DataSourceObserver {

    override fun onChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        adapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }
}