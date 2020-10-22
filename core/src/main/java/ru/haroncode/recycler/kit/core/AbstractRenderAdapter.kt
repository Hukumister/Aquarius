package ru.haroncode.recycler.kit.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.clicker.ClickableRenderer
import ru.haroncode.recycler.kit.core.clicker.Clicker

abstract class AbstractRenderAdapter<ItemModel : Any>(
    private val itemIdSelector: (ItemModel) -> Long,
    private val viewTypeSelector: (ItemModel) -> Int,
    private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    private val renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract val differ: Differ<ItemModel>

    override fun getItemId(position: Int): Long {
        val itemModel = differ.currentList[position]
        return itemIdSelector(itemModel)
    }

    override fun getItemViewType(position: Int): Int {
        val itemModel = differ.currentList[position]
        return viewTypeSelector(itemModel)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val itemViewType = viewHolder.itemViewType

        val renderer = findRenderer<RecyclerView.ViewHolder>(itemViewType)
        val itemModel = differ.currentList[position]
        renderer.onBindItemModel(viewHolder, itemModel, payloads)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = viewHolder.itemViewType

        val renderer = findRenderer<RecyclerView.ViewHolder>(itemViewType)
        val itemModel = differ.currentList[position]
        renderer.onBindItemModel(viewHolder, itemModel)
    }

    private fun <VH : RecyclerView.ViewHolder> findRenderer(itemViewType: Int): BaseRenderer<ItemModel, Any, VH> {
        @Suppress("UNCHECKED_CAST")
        val renderer = renderers.get(itemViewType) as? BaseRenderer<ItemModel, Any, VH>
        return checkNotNull(renderer) { "Unknown view type=$itemViewType" }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val renderer = findRenderer<RecyclerView.ViewHolder>(holder.itemViewType)
        if (renderer is ClickableRenderer) {
            bindClicker(renderer, holder)
        }
    }

    private fun <VH : RecyclerView.ViewHolder> bindClicker(renderer: ClickableRenderer, viewHolder: VH) {
        val itemClicker = findItemClicker<VH>(viewHolder.itemViewType)
        renderer.bindClickListener(viewHolder, ::onClick)
        itemClicker.onBindClicker(viewHolder)
    }

    private fun <VH : RecyclerView.ViewHolder> findItemClicker(itemViewType: Int): Clicker<Any, VH> {
        @Suppress("UNCHECKED_CAST")
        val itemClicker = clickers.get(itemViewType) as? Clicker<Any, VH>
        return checkNotNull(itemClicker) { "Unknown view type=$itemViewType" }
    }

    private fun <VH : RecyclerView.ViewHolder> onClick(viewHolder: VH, view: View) {
        val itemViewType = viewHolder.itemViewType
        val position = viewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val itemClicker = findItemClicker<VH>(itemViewType)
            val itemModel = differ.currentList[position]
            itemClicker.invoke(viewHolder, view, itemModel)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val adapterDelegate = findRenderer<RecyclerView.ViewHolder>(holder.itemViewType)
        if (adapterDelegate is ClickableRenderer) {
            unbindClicker(adapterDelegate, holder)
        }
    }

    private fun <VH : RecyclerView.ViewHolder> unbindClicker(renderer: ClickableRenderer, viewHolder: VH) {
        val itemClicker = findItemClicker<VH>(viewHolder.itemViewType)
        renderer.unbindClickListener(viewHolder)
        itemClicker.onUnbindClicker(viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val renderer = findRenderer<RecyclerView.ViewHolder>(viewType)
        val inflater = LayoutInflater.from(parent.context)
        return renderer.onCreateViewHolder(inflater, parent)
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        val itemViewType = viewHolder.itemViewType
        val renderer = findRenderer<RecyclerView.ViewHolder>(itemViewType)
        renderer.onRecycleViewHolder(viewHolder)
    }
}