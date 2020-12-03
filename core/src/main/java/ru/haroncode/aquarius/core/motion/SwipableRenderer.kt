package ru.haroncode.aquarius.core.motion

import androidx.recyclerview.widget.RecyclerView

interface SwipableRenderer {

    fun swipeDir(viewHolder: RecyclerView.ViewHolder): Int

    fun onItemStartSwipe(viewHolder: RecyclerView.ViewHolder) = Unit

    fun onSwiping(viewHolder: RecyclerView.ViewHolder, dX: Float, currentlyActive: Boolean) = Unit
}
