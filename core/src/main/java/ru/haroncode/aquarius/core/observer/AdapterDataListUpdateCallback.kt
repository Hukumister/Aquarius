package ru.haroncode.aquarius.core.observer

import androidx.recyclerview.widget.RecyclerView

class AdapterDataListUpdateCallback(
    private val adapter: RecyclerView.Adapter<*>
) : DataListUpdateCallback {

    override fun onChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position, count, payload)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }
}
