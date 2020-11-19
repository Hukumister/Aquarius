package ru.haroncode.aquarius.core.helper

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int, direction: Int)
}