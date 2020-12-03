package ru.haroncode.aquarius.core.observer

import androidx.annotation.IntRange

interface DataSourceObserver {

    fun onChanged()

    fun onItemRangeChanged(
        @IntRange(from = 0) positionStart: Int,
        @IntRange(from = 0) itemCount: Int,
        payload: Any? = null
    )

    fun onItemRangeInserted(
        @IntRange(from = 0) positionStart: Int,
        @IntRange(from = 0) itemCount: Int
    )

    fun onItemRangeRemoved(
        @IntRange(from = 0) positionStart: Int,
        @IntRange(from = 0) itemCount: Int
    )

    fun onItemMoved(
        @IntRange(from = 0) fromPosition: Int,
        @IntRange(from = 0) toPosition: Int
    )
}