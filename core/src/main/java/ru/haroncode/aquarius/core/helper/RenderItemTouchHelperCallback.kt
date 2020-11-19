package ru.haroncode.aquarius.core.helper

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.RenderAdapter
import ru.haroncode.aquarius.core.async.AsyncRenderAdapter

open class RenderItemTouchHelperCallback : ItemTouchHelper.Callback() {

    private lateinit var renderAdapter: RenderAdapter<*>

    fun attachAdapter(adapter: RenderAdapter<*>) {
        renderAdapter = adapter
    }

    @CallSuper
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val baseRenderer = renderAdapter.findRenderer<RecyclerView.ViewHolder>(viewHolder.itemViewType)
        val dragFlags = if (renderAdapter !is AsyncRenderAdapter) baseRenderer.dragDir(viewHolder) else 0
        val swipeFlags = baseRenderer.swipeDir(viewHolder)
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    @CallSuper
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
            renderAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        }
        return true
    }

    @CallSuper
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapterPosition = viewHolder.adapterPosition
        if (adapterPosition != RecyclerView.NO_POSITION) {
            renderAdapter.onItemDismiss(adapterPosition, direction)
        }
    }
}