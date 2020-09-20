package ru.haroncode.recycler.kit.core.clicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface Clicker<ItemModel, VH : RecyclerView.ViewHolder> {

    fun onBindClicker(holder: VH)

    fun invoke(holder: VH, view: View, item: ItemModel)

    fun onUnbindClicker(holder: VH)
}
