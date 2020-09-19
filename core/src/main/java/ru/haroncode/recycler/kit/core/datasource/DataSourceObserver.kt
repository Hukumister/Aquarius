package ru.haroncode.recycler.kit.core.datasource

import androidx.annotation.IntRange

interface DataSourceObserver {

    fun onChanged()

    fun onItemRangeChanged(
        @IntRange(from = 0) positionStart: Int,
        @IntRange(from = 0) itemCount: Int,
        payload: Any?
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