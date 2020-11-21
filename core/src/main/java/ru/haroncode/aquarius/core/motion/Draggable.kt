package ru.haroncode.aquarius.core.motion

import androidx.recyclerview.widget.RecyclerView

interface Draggable {

    fun dragDir(viewHolder: RecyclerView.ViewHolder): Int

    fun onItemStartDrag(viewHolder: RecyclerView.ViewHolder) = Unit

    fun onDragging(viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, currentlyActive: Boolean) = Unit
}