package ru.haroncode.aquarius.core.observer

import androidx.recyclerview.widget.ListUpdateCallback

interface DataListUpdateCallback : ListUpdateCallback {

    /**
     * Update all list. Be careful if you invoke this method, because method
     * RecyclerAdapter.notifyDataSetChanged() will be invoked.
     */
    fun onChanged()
}
