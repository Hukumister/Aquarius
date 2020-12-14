package ru.haroncode.aquarius.core.clicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ClickableRenderer {

    fun bindClickListener(
        viewHolder: RecyclerView.ViewHolder,
        listener: (RecyclerView.ViewHolder, View) -> Unit
    ) = viewHolder.itemView.setOnClickListener { view -> listener(viewHolder, view) }

    fun unbindClickListener(viewHolder: RecyclerView.ViewHolder) = viewHolder.itemView.setOnClickListener(null)
}
