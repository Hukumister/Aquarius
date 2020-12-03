package ru.haroncode.aquarius.core.helper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.RenderAdapter
import ru.haroncode.aquarius.core.async.AsyncRenderAdapter
import ru.haroncode.aquarius.core.motion.DraggableRenderer
import ru.haroncode.aquarius.core.motion.SwipableRenderer

open class RenderItemTouchHelperCallback : ItemTouchHelper.Callback() {

    private lateinit var renderAdapter: RenderAdapter<*>

    fun attachAdapter(adapter: RenderAdapter<*>) {
        renderAdapter = adapter
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val baseRenderer = renderAdapter.findRenderer<RecyclerView.ViewHolder>(viewHolder.itemViewType)
        when {
            actionState == ACTION_STATE_DRAG && baseRenderer is DraggableRenderer -> {
                baseRenderer.onDragging(viewHolder, dX, dY, isCurrentlyActive)
            }
            actionState == ACTION_STATE_SWIPE && baseRenderer is SwipableRenderer -> {
                baseRenderer.onSwiping(viewHolder, dX, isCurrentlyActive)
            }
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null) {
            onSelectedChangedInternal(viewHolder, actionState)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val baseRenderer = renderAdapter.findRenderer<RecyclerView.ViewHolder>(viewHolder.itemViewType)
        baseRenderer.onStopMoving(viewHolder)
    }

    private fun onSelectedChangedInternal(viewHolder: RecyclerView.ViewHolder, actionState: Int) {
        val baseRenderer = renderAdapter.findRenderer<RecyclerView.ViewHolder>(viewHolder.itemViewType)
        when {
            actionState == ACTION_STATE_DRAG && baseRenderer is DraggableRenderer -> baseRenderer.onItemStartDrag(viewHolder)
            actionState == ACTION_STATE_SWIPE && baseRenderer is SwipableRenderer -> baseRenderer.onItemStartSwipe(viewHolder)
        }
    }

    final override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val baseRenderer = renderAdapter.findRenderer<RecyclerView.ViewHolder>(viewHolder.itemViewType)
        val supportDragAndDrop = renderAdapter !is AsyncRenderAdapter
        val dragFlags = if (supportDragAndDrop && baseRenderer is DraggableRenderer) baseRenderer.dragDir(viewHolder) else 0
        val swipeFlags = if (baseRenderer is SwipableRenderer) baseRenderer.swipeDir(viewHolder) else 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    final override fun onMove(
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

    final override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapterPosition = viewHolder.adapterPosition
        if (adapterPosition != RecyclerView.NO_POSITION) {
            renderAdapter.onItemDismiss(adapterPosition, direction)
        }
    }
}
