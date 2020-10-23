package ru.haroncode.aquarius.core.renderer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseRenderer<ItemModel, RC, VH : RecyclerView.ViewHolder> {

    abstract fun getItem(itemModel: ItemModel): RC

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    abstract fun onBindView(viewHolder: VH, item: RC)

    open fun onBindView(viewHolder: VH, item: RC, payloads: List<Any?>) {
        onBindView(viewHolder, item)
    }

    internal fun onRecycleViewHolder(holder: RecyclerView.ViewHolder) {
        val viewHolder = holder as VH
        onRecycle(viewHolder)
    }

    internal fun onBindItemModel(holder: RecyclerView.ViewHolder, itemModel: ItemModel) {
        val viewHolder = holder as VH
        val item = getItem(itemModel)
        onBindView(viewHolder, item)
    }

    internal fun onBindItemModel(holder: RecyclerView.ViewHolder, itemModel: ItemModel, payloads: List<Any?>) {
        val viewHolder = holder as VH
        val item = getItem(itemModel)
        onBindView(viewHolder, item, payloads)
    }

    open fun onRecycle(viewHolder: VH) = Unit
}