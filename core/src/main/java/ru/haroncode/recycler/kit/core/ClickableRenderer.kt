package ru.haroncode.recycler.kit.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

typealias OnViewHolderClickListener = (RecyclerView.ViewHolder, View) -> Unit

interface ClickableRenderer {

    fun bindClickListener(viewHolder: RecyclerView.ViewHolder, listener: OnViewHolderClickListener) =
        viewHolder.itemView.setOnClickListener { view -> listener(viewHolder, view) }

    fun unbindClickListener(viewHolder: RecyclerView.ViewHolder) = viewHolder.itemView.setOnClickListener(null)
}
