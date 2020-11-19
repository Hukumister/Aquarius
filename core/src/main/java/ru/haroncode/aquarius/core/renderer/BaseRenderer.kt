package ru.haroncode.aquarius.core.renderer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class BaseRenderer<T, RC, VH : RecyclerView.ViewHolder> {

    open fun getItem(itemModel: T): RC = itemModel as RC

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    abstract fun onBindView(viewHolder: VH, item: RC)

    open fun swipeDir(viewHolder: VH): Int = 0

    open fun dragDir(viewHolder: VH): Int = 0

    open fun onBindView(viewHolder: VH, item: RC, payloads: List<Any?>) {
        onBindView(viewHolder, item)
    }

    internal fun onRecycleViewHolder(holder: RecyclerView.ViewHolder) {
        val viewHolder = holder as VH
        onRecycle(viewHolder)
    }

    internal fun onBindItemModel(holder: RecyclerView.ViewHolder, itemModel: T) {
        val viewHolder = holder as VH
        val item = getItem(itemModel)
        onBindView(viewHolder, item)
    }

    internal fun onBindItemModel(holder: RecyclerView.ViewHolder, itemModel: T, payloads: List<Any?>) {
        val viewHolder = holder as VH
        val item = getItem(itemModel)
        onBindView(viewHolder, item, payloads)
    }

    open fun onRecycle(viewHolder: VH) = Unit
}